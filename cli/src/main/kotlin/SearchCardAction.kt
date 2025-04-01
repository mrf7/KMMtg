import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Text
import kotlinx.coroutines.launch

@Composable
fun SearchCardAction(viewModel: CliViewModel, onComplete: () -> Unit) {
    var line by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Column {
        Text("Card Search Query:\n")
        TextInput(
            line,
            color = Color.Green,
            onEnter = {
                if (!loading) {
                    scope.launch {
                        loading = true
                        viewModel.searchAndAddCards(line)
                        loading = false
                        onComplete()
                    }
                }
            },
        ) {
            line = it
        }
    }
}
