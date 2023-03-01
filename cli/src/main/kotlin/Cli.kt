import androidx.compose.runtime.*
import client.ScryfallApi
import com.jakewharton.mosaic.Color
import com.jakewharton.mosaic.Column
import com.jakewharton.mosaic.Text
import com.jakewharton.mosaic.runMosaic
import com.mfriend.db.Card
import com.mfriend.db.DatabaseHelper
import com.mfriend.db.databaseModule
import kotlinx.coroutines.*
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin
import kotlin.system.exitProcess

enum class Action(val text: String) {
    Search("Add Card By Search"),
    Parse("Translate CardCastle file"),
    ViewCollection("View Collection"),
    Exit("Exit")
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
                Action.Parse -> Parse { activeAction = null }
                Action.ViewCollection -> Collection(db) { activeAction = null }
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
fun Parse(onComplete: Event) {
    var input by mutableStateOf("")
    Text("Filepath: $input")
    TrackInput({ input = it }) {
        onComplete()
    }
}

@Composable
fun Collection(database: DatabaseHelper, onComplete: Event) {
    val cards by database.getCards().collectAsState(emptyList())
    Column {
        Text("Your Collection")
        for (card in cards) {
            Text(card.toString())
        }
    }
    TrackKeys('b' to onComplete, onBackspace = onComplete)
}

@Composable
fun ActionSelection(onSelect: (Action) -> Unit) {
    var selection by mutableStateOf(0)
    Column {
        for (action in Action.values()) {
            Text(action.text, background = Color.White.takeIf { selection == action.ordinal })
        }
    }
    TrackKeys(
        'q' to { exitProcess(0) },
        upArrow = { selection = (selection - 1).coerceAtLeast(0) },
        downArrow = { selection = (selection + 1).coerceAtMost(Action.values().size - 1) },
        onEnter = { onSelect(Action.values()[selection]) },
        onSpace = { onSelect(Action.values()[selection]) }
    )
}

@Composable
fun SearchCardAction(api: ScryfallApi, database: DatabaseHelper, onComplete: () -> Unit) {
    var line by remember { mutableStateOf(" ") }
    Column {
        Text(line)
    }
    TrackInput({ line = it }) { input ->
        api.searchCard(input)
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

fun initKoin(): KoinApplication = startKoin {
    modules(scryfallModule, databaseModule)
}
