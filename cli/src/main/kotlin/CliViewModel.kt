import arrow.core.continuations.either
import arrow.core.getOrElse
import arrow.core.getOrHandle
import client.ScryfallApi
import com.mfriend.collection.CollectionImporter
import com.mfriend.db.Card
import com.mfriend.db.DatabaseHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.yield
import models.CardDto
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder

sealed interface KeyStroke
enum class Thing : KeyStroke {
    Delete,
    Space,
    Enter,
    UpArrow,
    DownArrow,
}

class Character(code: Int) : KeyStroke {
    val letter: Char = code.toChar()
}

class CliViewModel(
    private val importer: CollectionImporter,
    private val database: DatabaseHelper,
    private val api: ScryfallApi,
) {
    private val scope = CoroutineScope(SupervisorJob())
    val keyStrokes: SharedFlow<KeyStroke> = flow {
        val terminal: Terminal = TerminalBuilder.terminal()
        terminal.enterRawMode()
        val reader = terminal.reader()
        while (true) {
            yield()
            when (val key = reader.read()) {
                in '!'.code..'z'.code -> emit(Character(key))
                27 -> {
                    when (reader.read()) {
                        91 -> {
                            when (reader.read()) {
                                // Up arrow
                                65 -> emit(Thing.UpArrow)
                                // Down arrow
                                66 -> emit(Thing.DownArrow)
                            }
                        }
                    }
                }
                // Enter
                13 -> emit(Thing.Enter)
                32 -> emit(Thing.Space)
                127 -> emit(Thing.Delete)
            }
        }
    }
        .buffer(3, BufferOverflow.DROP_OLDEST)
        .shareIn(scope, SharingStarted.WhileSubscribed(20))

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
        succ.getOrHandle { throw Exception(it) }
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
                delay(500)
                database.insertCard(card)
            }.mapLeft { throw Exception(it) }
    }

    fun getCards() = database.getCards()

    suspend fun searchCards(query: String): List<CardDto>? = api.searchCard(query).getOrElse { null }
}
