import androidx.compose.runtime.*
import arrow.core.continuations.either
import arrow.core.getOrHandle
import arrow.core.handleError
import client.ScryfallApi
import com.jakewharton.mosaic.Color
import com.jakewharton.mosaic.Column
import com.jakewharton.mosaic.Text
import com.jakewharton.mosaic.runMosaic
import com.mfriend.collection.CollectionImporter
import com.mfriend.collection.CollectionImporterImpl
import com.mfriend.db.Card
import com.mfriend.db.DatabaseHelper
import com.mfriend.db.databaseModule
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.security.Key
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.system.exitProcess

enum class Action(val text: String) {
    Search("Add Card By Search"),
    Parse("Translate CardCastle file"),
    ViewCollection("View Collection"),
    Exit("Exit")
}

class CliViewModel(
    private val importer: CollectionImporter,
    private val database: DatabaseHelper,
    private val api: ScryfallApi,
    context: CoroutineContext
) {
    private val scope = CoroutineScope(context + SupervisorJob(context.job))
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

    suspend fun searchCard(query: String) {
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

}

sealed interface KeyStroke
enum class Thing() : KeyStroke {
    Delete, Space, Enter, UpArrow, DownArrow
}

class Character(code: Int) : KeyStroke {
    val letter: Char = code.toChar()
}

suspend fun main() = runMosaic {
    var activeAction by mutableStateOf<Action?>(null)
    val koin = initKoin().koin
    val viewModel: CliViewModel = koin.get { parametersOf(coroutineContext) }

    setContent {
        when (activeAction) {
            Action.Search -> SearchCardAction(viewModel) { activeAction = null }
            Action.Parse -> Parse(viewModel) { activeAction = null }
            Action.ViewCollection -> Collection(viewModel) { activeAction = null }
            Action.Exit -> exitProcess(0)
            null -> ActionSelection(viewModel) { activeAction = it }
        }
    }
    withContext(Dispatchers.IO) {
        while (isActive) {
            // Wait loop
        }
    }
}

@Composable
fun Parse(viewModel: CliViewModel, onComplete: Event) {
    var input by mutableStateOf("")
    Column {
        Text("Filepath: ${input.trim()}")
    }
    TrackInputFlow(viewModel, { input = it }) {
        viewModel.translateCsv(it)
        onComplete()
    }
}

@Composable
fun Collection(viewModel: CliViewModel, onComplete: Event) {
    val cards by viewModel.getCards().collectAsState(emptyList())
    Column {
        Text("Your Collection")
        for (card in cards) {
            Text(card.toString())
        }
    }
    TrackKeysFlow(viewModel, 'b' to onComplete, onBackspace = onComplete)
}

@Composable
fun ActionSelection(viewModel: CliViewModel, onSelect: (Action) -> Unit) {
    var selection by mutableStateOf(0)
    Column {
        for (action in Action.values()) {
            Text(action.text, background = Color.White.takeIf { selection == action.ordinal })
        }
    }
    TrackKeysFlow(
        viewModel,
        'q' to { exitProcess(0) },
        upArrow = { selection = (selection - 1).coerceAtLeast(0) },
        downArrow = { selection = (selection + 1).coerceAtMost(Action.values().size - 1) },
        onEnter = { onSelect(Action.values()[selection]) },
        onSpace = { onSelect(Action.values()[selection]) }
    )
}

@Composable
fun SearchCardAction(viewModel: CliViewModel, onComplete: () -> Unit) {
    var line by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    Column {
        Text("Card Search Query:\n $line")
        TrackInputFlow(viewModel, { line = it }) { input ->
            if (loading) return@TrackInputFlow
            loading = true
            viewModel.searchCard(input)
            loading = false
            onComplete()
        }
    }
}

fun initKoin(): KoinApplication = startKoin {
    modules(scryfallModule, databaseModule, cliModule)
}

private val cliModule = module {
    singleOf(::CliViewModel)
    singleOf(::CollectionImporterImpl) bind CollectionImporter::class
}
