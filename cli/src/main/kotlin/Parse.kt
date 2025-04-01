import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Text

@Composable
fun Parse(viewModel: CliViewModel, onComplete: () -> Unit) {
    var input by mutableStateOf("")
    Column {
        Text("Filepath: ${input.trim()}")
    }
    // TODO MRF fix parse input
//    TrackInputFlow(viewModel, { input = it }) {
//        viewModel.translateCsv(it)
//        onComplete()
//    }
}
