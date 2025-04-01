import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.layout.KeyEvent
import com.jakewharton.mosaic.layout.onKeyEvent
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Text
import com.jakewharton.mosaic.ui.TextStyle

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
}
