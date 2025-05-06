package client

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.Raise
import arrow.core.right
import bind
import co.touchlab.kermit.Logger
import ensure
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import io.ktor.http.takeFrom
import models.CardDto
import models.ListResp
import models.SetDto

interface ScryfallApi : AutoCloseable {
    suspend fun cardNamed(name: String): Either<String, CardDto>
    suspend fun searchCard(searchParam: String): Either<String, List<CardDto>>
    suspend fun sets(): Either<String, List<SetDto>>
}

suspend inline fun <reified T> HttpResponse.toEither(): Either<String, T> = when (this.status.value) {
    in 200..299 -> this.body<T>().right()
    else -> this.body<String>().left()
}

context(_: Raise<Throwable>)
inline fun <T> catch(block: () -> T): T {
    return Either.catch(block).bind()
}

class ScryfallApiImpl : ScryfallApi, AutoCloseable {
    private val client = newKtorClient()
    override suspend fun cardNamed(name: String): Either<String, CardDto> {
        val response = client.get {
            scryfall("$CardApiBase$FindNamed")
            parameter("fuzzy", name)
        }

        return response.toEither()
    }

    // This NEEDS to be called or it will hold up the couroutine scope of suspend fun main
    override fun close() {
        client.close()
    }

    context(_: Raise<Throwable>)
    suspend fun searchCardRaise(searchParam: String): List<CardDto> {
        return catch {
            val response = client.get {
                scryfall("$CardApiBase$Search")
                parameter("q", searchParam)
            }
            ensure(response.status.isSuccess()) {
                IllegalStateException(response.body<String>())
            }
            response.body<ListResp<CardDto>>().data
        }
    }

    override suspend fun searchCard(searchParam: String): Either<String, List<CardDto>> {
        val resp: HttpResponse = client.get {
            scryfall("$CardApiBase$Search")
            parameter("q", searchParam)
        }

        return resp
            .toEither<ListResp<CardDto>>()
            .onRight { if (it.hasMore) Logger.d("Scryfall") { "Search has more" } }
            .map { it.data }
    }

    override suspend fun sets(): Either<String, List<SetDto>> {
        val resp: HttpResponse = client.get {
            scryfall("sets")
        }
        return resp
            .toEither<ListResp<SetDto>>()
            .map { it.data }
    }

    companion object {
        const val ScryfallBaseUri: String = "https://api.scryfall.com"
        const val CardApiBase: String = "/cards"
        const val FindNamed: String = "/named"
        const val Search: String = "/search"
    }

    private fun HttpRequestBuilder.scryfall(path: String) {
        url {
            takeFrom(ScryfallBaseUri)
            encodedPath = path
        }
    }
}
