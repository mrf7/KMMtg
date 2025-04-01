import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Text
import models.CardDto

@Composable
fun SetCube(viewModel: CliViewModel, onComplete: ()-> Unit) {
    var input by mutableStateOf("")
    var res: List<CardDto>? by remember { mutableStateOf(emptyList()) }
    Column {
        res?.let {
            Text(it.size.toString())
            Text(it.sumOf { it.prices.usd?.toDoubleOrNull() ?: 0.0 }.toString())
        }
        Text("Set code: ${input.trim()}")
    }

    // TODO Get input
//    TrackInputFlow(viewModel, { input = it }) {
//        res = viewModel.searchCards("s:$it unique=cards is:booster")
// //        onComplete()
//    }
}