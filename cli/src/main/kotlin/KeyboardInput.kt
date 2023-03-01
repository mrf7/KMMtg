import androidx.compose.runtime.*
import kotlinx.coroutines.*
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder

typealias Event = () -> Unit

@Composable
internal fun TrackKeys(
    vararg actions: Pair<Char, Event>,
    upArrow: Event = {},
    downArrow: Event = {},
    onEnter: Event = {},
    onSpace: Event = {},
    onBackspace: Event = {},
) {
    LaunchedEffect(true) {
        withContext(Dispatchers.IO) {
            val terminal: Terminal = TerminalBuilder.terminal()
            terminal.enterRawMode()
            val reader = terminal.reader()
            val trackedKeys = actions.associate { it.first.code to it.second }
            while (isActive) {
                when (val key = reader.read()) {
                    in trackedKeys.keys -> trackedKeys[key]?.let { it() }
                    27 -> {
                        when (reader.read()) {
                            91 -> {
                                when (reader.read()) {
                                    // Up arrow
                                    65 -> upArrow()
                                    // Down arrow
                                    66 -> downArrow()
                                }
                            }
                        }
                    }
                    // Enter
                    13 -> onEnter()
                    32 -> onSpace()
                    127 -> onBackspace()
                }
            }
        }
    }
}

@Composable
internal fun TrackInput(onInputUpdate: (String) -> Unit, onEnter: suspend (String) -> Unit) {
    var line by mutableStateOf(" ")
    LaunchedEffect(true) {
        withContext(Dispatchers.IO) {
            val terminal: Terminal = TerminalBuilder.terminal()
            terminal.enterRawMode()
            val reader = terminal.reader()
            while (isActive) {
                when (val key = reader.read()) {
                    in 'A'.code..'z'.code -> {
                        line += key.toChar()
                        onInputUpdate(line)
                    }

                    13 -> {
                        launch {
                            onEnter(line.trim())
                        }
                    }

                }
            }
        }

    }
}