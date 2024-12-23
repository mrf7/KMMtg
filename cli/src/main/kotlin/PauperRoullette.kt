import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import arrow.core.getOrElse
import client.ScryfallApi
import com.jakewharton.mosaic.Column
import com.jakewharton.mosaic.Text
import kotlinx.coroutines.delay
import models.SetDto
import org.koin.core.component.getScopeId
import java.io.File.separator

@Composable
fun PauperRoullete(api: ScryfallApi, onComplete: () -> Unit) {
    var sets by remember { mutableStateOf(emptyList<SetDto>()) }
    LaunchedEffect(Unit) {
        sets = api.sets().map { sets ->
            sets.filter {
                it.digital && it.setType in listOf(
                    "core",
                    "expansion",
//                    "masters",
//                    "draft_innovation"
                ) && it.arenaCode != it.code
            }
        }.map {
            it.shuffled().take(3)
        }.getOrElse { error("got an error") }
        delay(500)
        onComplete()
    }
    Column {
        Text("Sets: " + sets.joinToString(separator = "\n") { "${it.name} (${it.arenaCode})" })
        Text(sets.joinToString { it.scryfallUri })
    }
}