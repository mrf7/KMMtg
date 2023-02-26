import arrow.core.Either
import client.ScryfallApi
import commfrienddb.Card
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin

suspend fun main() = coroutineScope {
    val koin = initKoin().koin
    val client: ScryfallApi = koin.get()
    val databaseHelper:DatabaseHelper = koin.get()
    launch {
        databaseHelper.getCardsFlow().collect {
            println(it)
            println()
        }
    }
    while (true) {
        println("1) Add Card Named\n2) Add Card By Search String\n3) Exit\n")
        val selection = readln().toInt()
        println("input: ")
        val result = when (selection) {
            1 -> getCardNamed(client)
            2 -> searchCard(client)
            else -> break
        }
        result.map { databaseHelper.insertCard(it) }
    }
}

suspend fun getCardNamed(client: ScryfallApi): Either<String, Card> {
    return client.cardNamed(readln()).map {
        Card(it.name, it.set, it.setName, it.imageUris?.large?.url, it.scryfallUrl.url, it.prices.usd.toDouble())
    }
}

suspend fun searchCard(client: ScryfallApi): Either<String, Card> {
    return client.searchCard(readln()).map {
        it.singleOrNull()?.let { selection ->
            return@map Card(
                selection.name,
                selection.set,
                selection.setName,
                selection.imageUris?.large?.url,
                selection.scryfallUrl.url,
                selection.prices.usd.toDouble()
            )
        }

        it.forEachIndexed { i, card ->
            println("$i) $card")
        }
        val selection = it[readln().toInt()]
        Card(
            selection.name,
            selection.set,
            selection.setName,
            selection.imageUris?.large?.url,
            selection.scryfallUrl.url,
            selection.prices.usd.toDouble()
        )
    }
}

fun initKoin(): KoinApplication = startKoin {
    modules(scryfallModule, databaseModule)
}
