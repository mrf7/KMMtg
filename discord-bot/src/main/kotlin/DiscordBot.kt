import com.mfriend.db.databaseModule
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin

suspend fun main() = coroutineScope {

}

fun initKoin(): KoinApplication = startKoin {
    modules(databaseModule, scryfallModule)
}