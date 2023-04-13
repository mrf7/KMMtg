import com.mfriend.db.databaseModule
import com.mfriend.scryfall.db.CacheDb
import com.mfriend.scryfall.db.cacheDb
import com.mfriend.scryfallModule
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext
import java.io.File

suspend fun main() {
    val data = File("small-export.json").readText()
    val cards = cacheDb(data)
    println(cards.first())
}
