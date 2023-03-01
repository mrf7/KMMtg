import androidx.compose.runtime.*
import arrow.core.Either
import client.ScryfallApi
import com.jakewharton.mosaic.Color
import com.jakewharton.mosaic.Column
import com.jakewharton.mosaic.Text
import com.jakewharton.mosaic.runMosaic
import com.mfriend.db.Card
import kotlinx.coroutines.*
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin
import kotlin.system.exitProcess

enum class Action(val text: String) {
    Named("Add for card Named"), Search("Add Card By Search"), Parse("Translate CardCastle file"), ViewCollection("View Collection"), Exit(
        "Exit"
    )
}


suspend fun main() = runMosaic {
    var activeAction by mutableStateOf<Action?>(null)
    val koin = initKoin().koin
    val api: ScryfallApi = koin.get()
    val db: DatabaseHelper = koin.get()

    setContent {
        if (activeAction == null) {
            ActionSelection { activeAction = it }
        } else {
            when (activeAction) {
                Action.Search -> SearchCardAction(api, db) { activeAction = null }
                Action.Named -> TODO()
                Action.Parse -> TODO()
                Action.ViewCollection -> Collection(db)
                Action.Exit -> exitProcess(0)
                null -> TODO()
            }
        }
    }
    withContext(Dispatchers.IO) {
        while (isActive) {
            // Wait loop
        }
    }
}

@Composable
fun Collection(database: DatabaseHelper) {
    val cards by database.getCards().collectAsState(emptyList())
    Column {
        Text("Your Collection")
        for (card in cards) {
            Text(card.toString())
        }
    }
}

@Composable
fun ActionSelection(onSelect: (Action) -> Unit) {
    var selection by mutableStateOf(Action.Named.ordinal)
    Column {
        for (action in Action.values()) {
            Text(action.text, background = Color.White.takeIf { selection == action.ordinal })
        }
    }
    LaunchedEffect(true) {
        withContext(Dispatchers.IO) {
            val terminal: Terminal = TerminalBuilder.terminal()
            terminal.enterRawMode()
            val reader = terminal.reader()
            while (isActive) {
                when (reader.read()) {
                    'q'.code -> break
                    27 -> {
                        when (reader.read()) {
                            91 -> {
                                when (reader.read()) {
                                    // Up arrow
                                    65 -> selection = (selection - 1).coerceAtLeast(0)
                                    // Down arrow
                                    66 -> selection = (selection + 1).coerceAtMost(Action.values().size - 1)
                                }
                            }
                        }
                    }
                    // Enter
                    13 -> onSelect(Action.values()[selection])
                }
            }
        }
    }
}

@Composable
fun SearchCardAction(api: ScryfallApi, database: DatabaseHelper, onComplete: () -> Unit) {
    var line by remember { mutableStateOf(" ") }
    Column {
        Text(line)
    }
    LaunchedEffect(true) {
        withContext(Dispatchers.IO) {
            val terminal: Terminal = TerminalBuilder.terminal()
            terminal.enterRawMode()
            val reader = terminal.reader()
            while (isActive) {
                when (val key = reader.read()) {
                    in 'A'.code..'z'.code -> {
                        line += key.toChar()
                    }

                    13 -> {
                        launch {
                            api.searchCard(line.trim())
                                .map {
                                    val selection = it.first()
                                    val card = Card(
                                        selection.name,
                                        selection.set,
                                        selection.setName,
                                        selection.imageUris?.large?.url,
                                        selection.scryfallUrl.url,
                                        selection.prices.usd.toDouble()
                                    )
                                    delay(500)
                                    database.insertCard(card)
                                    onComplete()
                                }.mapLeft { throw Exception(it) }
                        }
                    }

                }
            }
        }

    }
}

fun initKoin(): KoinApplication = startKoin {
    modules(scryfallModule, databaseModule)
}
