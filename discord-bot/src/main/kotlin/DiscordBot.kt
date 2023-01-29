import client.ScryfallApiImpl
import commfrienddb.Card
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main() = coroutineScope {
    val client = ScryfallApiImpl()
    val databaseHelper = DatabaseHelper()
    val job = launch {
        databaseHelper.getCards().collect {
            println(it)
        }
    }
    delay(1000)
    client.cardNamed("chatterfang")
        .map {
            it.apply {
                databaseHelper.insertCard(
                    Card(name, set, setName, imageUris?.large?.url, scryfallUrl.url, prices.usd.toDouble())
                )
            }
        }
    delay(100)
    job.cancel()
}