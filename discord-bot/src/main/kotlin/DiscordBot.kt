import com.mfriend.platform.client.ScryfallApiImpl
import com.mfriend.platform.getPlatform
import java.net.http.HttpClient

suspend fun main() {
    val client = ScryfallApiImpl()
    println(client.searchCard("Chatterfang"))
}