import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.ui.Column
import kotlinx.coroutines.launch

@Composable
fun Parse(viewModel: CliViewModel, onComplete: () -> Unit) {
    var input by mutableStateOf("")
    val scope = rememberCoroutineScope()
    Column {
        TextInput("Filepath: ${input.trim()}", onEnter = {
            scope.launch {
                viewModel.translateCsv(input)
                onComplete()
            }
        }) {
            input = it.trim()
        }
    }
}
