import arrow.core.getOrElse
import arrow.core.raise.either
import client.ScryfallApi
import com.mfriend.collection.CollectionImporter
import com.mfriend.db.Card
import com.mfriend.db.DatabaseHelper
import models.CardDto

class CliViewModel(
    private val importer: CollectionImporter,
    private val database: DatabaseHelper,
    private val api: ScryfallApi,
    private val composeLogger: ComposeLogger,
) {
    val logs = composeLogger.logs
    suspend fun translateCsv(filePath: String) {
        val succ = either {
            val imported = importer.parseCardCastle(filePath).bind()
            val cardRows = imported.map { importCard ->
                api.searchCard("${importCard.name} s:${importCard.set} cn:\"${importCard.number}\"").bind()
                    .first() to importCard.count
            }.map { (card, count) ->
                Card(
                    card.name,
                    card.set,
                    card.setName,
                    card.imageUris?.large?.url,
                    card.scryfallUrl.url,
                ) to count
            }
            cardRows.forEach { (card, count) ->
                repeat(count) { database.insertCard(card) }
            }
            return@either cardRows.size
        }
        succ.getOrElse { throw Exception(it) }
    }

    suspend fun searchAndAddCards(query: String) {
        api.searchCard(query)
            .map {
                val selection = it.first()
                val card = Card(
                    selection.name,
                    selection.set,
                    selection.setName,
                    selection.imageUris?.large?.url,
                    selection.scryfallUrl.url,
                )
                database.insertCard(card)
            }.mapLeft { throw Exception(it) }
    }

    fun getCards() = database.getCards()

    suspend fun searchCards(query: String): List<CardDto>? = api.searchCard(query).getOrElse { null }
}
