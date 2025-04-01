import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import co.touchlab.kermit.Logger
import com.jakewharton.mosaic.layout.KeyEvent
import com.jakewharton.mosaic.layout.onKeyEvent
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.runMosaic
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Text
import com.mfriend.collection.CollectionImporter
import com.mfriend.collection.CollectionImporterImpl
import com.mfriend.db.databaseModule
import kotlinx.coroutines.awaitCancellation
import models.CardDto
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.system.exitProcess

enum class Action(val text: String) {
    Search("Add Card By Search"),
    Parse("Translate CardCastle file"),
    ViewCollection("View Collection"),
    BuildCube("Build Cube"),
    Exit("Exit"),
}

suspend fun main() = runMosaic {
    KoinApplication(application = {
        Logger.setLogWriters()
        modules(scryfallModule, databaseModule, cliModule)
    }) {
        var activeAction by mutableStateOf<Action?>(null)
        val viewModel: CliViewModel = koinInject()

        when (activeAction) {
            Action.Search -> SearchCardAction(viewModel) { activeAction = null }
            Action.Parse -> Parse(viewModel) { activeAction = null }
            Action.ViewCollection -> Collection(viewModel) { activeAction = null }
            Action.BuildCube -> SetCube(viewModel) { activeAction = null }
            Action.Exit -> exitProcess(0)
            null -> ActionSelection(viewModel) { activeAction = it }
        }
        LaunchedEffect(Unit) {
            awaitCancellation()
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
fun SetCube(viewModel: CliViewModel, onComplete: Event) {
    var input by mutableStateOf("")
    var res: List<CardDto>? by remember { mutableStateOf(emptyList()) }
    Column {
        res?.let {
            Text(it.size.toString())
            Text(it.sumOf { it.prices.usd?.toDoubleOrNull() ?: 0.0 }.toString())
        }
        Text("Set code: ${input.trim()}")
    }

    TrackInputFlow(viewModel, { input = it }) {
        res = viewModel.searchCards("s:$it unique=cards is:booster")
//        onComplete()
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
    Column(
        modifier = Modifier.onKeyEvent {
            println(it)
            selection = when (it) {
                KeyEvent("ArrowUp") -> (selection - 1).coerceAtLeast(0)
                KeyEvent("ArrowDown") -> (selection + 1).coerceAtLeast(0)
                KeyEvent("q") -> exitProcess(0)
                else -> return@onKeyEvent false
            }
            true
        },
    ) {
        for (action in Action.entries) {
            Text(action.text, background = Color.White.takeIf { selection == action.ordinal } ?: Color.Unspecified)
        }
    }
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
            viewModel.searchAndAddCards(input)
            loading = false
            onComplete()
        }
    }
}

private val cliModule = module {
    singleOf(::CliViewModel)
    singleOf(::CollectionImporterImpl) bind CollectionImporter::class
}
