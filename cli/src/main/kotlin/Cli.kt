import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.Color
import com.jakewharton.mosaic.Column
import com.jakewharton.mosaic.Text
import com.jakewharton.mosaic.runMosaic
import com.mfriend.collection.CollectionImporter
import com.mfriend.collection.CollectionImporterImpl
import com.mfriend.db.databaseModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import models.CardDto
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.system.exitProcess

enum class Action(val text: String) {
    Search("Add Card By Search"),
    Parse("Translate CardCastle file"),
    ViewCollection("View Collection"),
    BuildCube("Build Cube"),
    Exit("Exit")
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
            Action.BuildCube -> SetCube(viewModel) { activeAction = null }
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
            viewModel.searchAndAddCards(input)
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
