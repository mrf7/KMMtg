import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.layout.KeyEvent
import com.jakewharton.mosaic.layout.background
import com.jakewharton.mosaic.layout.onKeyEvent
import com.jakewharton.mosaic.layout.size
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Row
import com.jakewharton.mosaic.ui.Spacer
import com.jakewharton.mosaic.ui.Text
import com.jakewharton.mosaic.ui.TextStyle
import kotlinx.coroutines.delay

@Composable
fun TextInput(
    value: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    background: Color = Color.Unspecified,
    textStyle: TextStyle = TextStyle.Unspecified,
    onEnter: (() -> Unit) = {},
    onTextChanged: ((String) -> Unit),
) {
    Row {
        Text(
            value = value,
            color = color,
            background = background,
            textStyle = textStyle,
            modifier = modifier.onKeyEvent { event ->
                when {
                    event == KeyEvent("c", ctrl = true) -> return@onKeyEvent false
                    event.key.toCharArray().singleOrNull() != null -> onTextChanged(value + event.key)
                    event == KeyEvent("Backspace") -> onTextChanged(value.dropLast(1))
                    event == KeyEvent("Enter") -> onEnter()
                    else -> return@onKeyEvent false
                }
                true
            },
        )
        var cursorVisible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            while (true) {
                delay(500)
                cursorVisible = !cursorVisible
            }
        }
        Spacer(Modifier.background(if (cursorVisible) Gray else Color.Unspecified).size(1, 1))
    }
}

private val Gray = Color(128, 128, 128)
