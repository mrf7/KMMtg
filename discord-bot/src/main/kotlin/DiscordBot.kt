import arrow.core.raise.ExperimentalTraceApi
import arrow.core.raise.context.mapOrAccumulate
import client.ScryfallApiImpl

@OptIn(ExperimentalTraceApi::class)
suspend fun main() {
    ScryfallApiImpl().use { api ->
        boom(
            block = {
                listOf("black lotus", "").mapOrAccumulate { search ->
                    search.alsoLog { " Searching for $it" }
                    api.cardNamedRaise(search)
                }
            },
        ).alsoLog()
    }
}
