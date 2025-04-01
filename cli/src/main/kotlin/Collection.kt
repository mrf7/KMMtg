import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jakewharton.mosaic.layout.KeyEvent
import com.jakewharton.mosaic.layout.onKeyEvent
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Text

@Composable
fun Collection(viewModel: CliViewModel, onComplete: () -> Unit) {
    val cards by viewModel.getCards().collectAsState(emptyList())
    Column(
        modifier = Modifier.onKeyEvent {
            when (it) {
                KeyEvent("b") -> onComplete()
                KeyEvent("Backspace") -> onComplete()
            }
            true
        },
    ) {
        Text("Your Collection")
        for (card in cards) {
            Text(card.toString())
        }
    }
}
