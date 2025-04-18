import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import com.jakewharton.mosaic.layout.KeyEvent
import com.jakewharton.mosaic.layout.fillMaxWidth
import com.jakewharton.mosaic.layout.onKeyEvent
import com.jakewharton.mosaic.layout.width
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.runMosaic
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Row
import com.jakewharton.mosaic.ui.Spacer
import com.jakewharton.mosaic.ui.Text
import com.mfriend.collection.CollectionImporter
import com.mfriend.collection.CollectionImporterImpl
import com.mfriend.db.databaseModule
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
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
        Logger.setLogWriters(ComposeLogger)
        modules(scryfallModule, databaseModule, cliModule)
    }) {
        var activeAction by mutableStateOf<Action?>(null)
        val viewModel: CliViewModel = koinInject()
        Row(Modifier.fillMaxWidth()) {
            when (activeAction) {
                Action.Search -> SearchCardAction(viewModel) { activeAction = null }
                Action.Parse -> Parse(viewModel) { activeAction = null }
                Action.ViewCollection -> Collection(viewModel) { activeAction = null }
                Action.BuildCube -> SetCube(viewModel) { activeAction = null }
                Action.Exit -> exitProcess(0)
                null -> ActionSelection { activeAction = it }
            }
            Spacer(Modifier.width(50))
            Column {
                val logs by viewModel.logs.collectAsState()
                logs.takeLast(10).forEach {
                    Text(it.message, color = if (it.level == Severity.Error) Color.Red else Color.Unspecified)
                }
                LaunchedEffect(Unit) {
                    repeat(100) {
                        delay(500)
                        Logger.log(Severity.entries.random(), "Tag", null, "$it")
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            awaitCancellation()
        }
    }
}

@Composable
fun ActionSelection(onSelect: (Action) -> Unit) {
    var selection by remember { mutableStateOf(0) }
    var keyEvent by remember { mutableStateOf<KeyEvent?>(null) }
    Column(
        modifier = Modifier.onKeyEvent {
            keyEvent = it
            selection = when (it) {
                KeyEvent("ArrowUp") -> (selection - 1).coerceAtLeast(0)
                KeyEvent("ArrowDown") -> (selection + 1).coerceAtLeast(0)
                KeyEvent("q") -> exitProcess(0)
                KeyEvent(" "), KeyEvent("Enter") -> {
                    onSelect(Action.entries[selection])
                    return@onKeyEvent true
                }

                else -> return@onKeyEvent false
            }
            true
        },
    ) {
        Text(keyEvent?.toString() ?: "None")
        for (action in Action.entries) {
            Text(action.text, background = Color.White.takeIf { selection == action.ordinal } ?: Color.Unspecified)
        }
    }
}

private val cliModule = module {
    singleOf(::CliViewModel)
    singleOf(::CollectionImporterImpl) bind CollectionImporter::class
    single { ComposeLogger }
}
