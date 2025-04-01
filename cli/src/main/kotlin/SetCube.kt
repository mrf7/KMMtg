import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Text
import kotlinx.coroutines.launch
import models.CardDto

@Composable
fun SetCube(viewModel: CliViewModel, onComplete: () -> Unit) {
    var input by mutableStateOf("")
    var res: List<CardDto>? by remember { mutableStateOf(emptyList()) }
    val scope = rememberCoroutineScope()
    Column {
        res?.let {
            Text(it.size.toString())
            Text(it.sumOf { it.prices.usd?.toDoubleOrNull() ?: 0.0 }.toString())
        }
        TextInput("Set code: ${input.trim()}", onEnter = {
            scope.launch {
                res = viewModel.searchCards("s:$input unique=cards is:booster")
            }
        }) {
            input = it
        }
    }
}
