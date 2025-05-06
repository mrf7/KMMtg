import arrow.core.raise.either
import client.ScryfallApiImpl
import com.mfriend.db.databaseModule
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin

suspend fun main() {
    ScryfallApiImpl().use { scryfallApi ->
        val x = either {
            scryfallApi.searchCardRaise("")
        }
        println(x)
    }
}

fun initKoin(): KoinApplication = startKoin {
    modules(databaseModule, scryfallModule)
}
