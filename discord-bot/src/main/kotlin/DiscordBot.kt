import client.ScryfallApi
import client.ScryfallApiImpl
import commfrienddb.Card
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin

suspend fun main() = coroutineScope {

}

fun initKoin(): KoinApplication = startKoin {
    modules(databaseModule, scryfallModule)
}