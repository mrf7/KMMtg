import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Text

@Composable
fun Collection(viewModel: CliViewModel, onComplete: () -> Unit) {
    val cards by viewModel.getCards().collectAsState(emptyList())
    Column {
        Text("Your Collection")
        for (card in cards) {
            Text(card.toString())
        }
    }
    // TODO MRF fix xollection
//    TrackKeysFlow(viewModel, 'b' to onComplete, onBackspace = onComplete)
}
