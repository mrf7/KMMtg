import arrow.core.raise.ExperimentalTraceApi
import arrow.core.raise.recover
import client.ScryfallApiImpl

@OptIn(ExperimentalTraceApi::class)
suspend fun main() {
    ScryfallApiImpl().use { api ->
        recover(
            block = {
                listOf("black lotus", "").mapOrAccumulate { search ->
                    search.alsoLog { " Searching for $it" }
                    api.cardNamedRaise(search)
                }
            },
            recover = { it },
        ).alsoLog()
    }
}
