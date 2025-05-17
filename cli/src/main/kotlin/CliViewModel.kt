import arrow.core.NonEmptyList
import arrow.core.getOrElse
import arrow.core.raise.either
import arrow.core.raise.nullable
import arrow.core.raise.recover
import arrow.core.toNonEmptyListOrNull
import client.ScryfallApi
import co.touchlab.kermit.Logger
import com.mfriend.collection.CollectionImporter
import com.mfriend.db.Card
import com.mfriend.db.DatabaseHelper
import models.CardDto

class CliViewModel(
    private val importer: CollectionImporter,
    private val database: DatabaseHelper,
    private val api: ScryfallApi,
    composeLogger: ComposeLogger,
) {
    val logs = composeLogger.logs

    // TODO Replace with raise and expose results to UI
    suspend fun translateCsv(filePath: String) {
        val succ = either {
            val imported = importer.parseCardCastle(filePath).bind()
            val cardRows = imported.map { importCard ->
                val results = withErrorString {
                    api.searchCardRaise("${importCard.name} s:${importCard.set} cn:\"${importCard.number}\"")
                }
                results.first() to importCard.count
            }.map { (card, count) ->
                Card(
                    -1, // TODO Fix insert
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
        // TODO make this pure or something
        recover({
            val results = api.searchCardRaise(query)
            val selection = results.first()
            val card = Card(
                -1, // TODO Fix insert
                selection.name,
                selection.set,
                selection.setName,
                selection.imageUris?.large?.url,
                selection.scryfallUrl.url,
            )
            database.insertCard(card)
        }, { Logger.e(it.toString()) })
    }

    fun getCards() = database.getCards()

    suspend fun searchCards(query: String): NonEmptyList<CardDto>? = nullable {
        ignoreErrors { api.searchCardRaise(query).toNonEmptyListOrNull() }
    }
}
