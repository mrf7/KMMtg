package client

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.withError
import arrow.core.right
import bind
import co.touchlab.kermit.Logger
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
import models.ScryfallError
import models.SetDto
import raise

interface ScryfallApi : AutoCloseable {
    context(_: Raise<ScryfallError>)
    suspend fun cardNamedRaise(name: String): CardDto

    context(_: Raise<ScryfallError>)
    suspend fun searchCardRaise(searchParam: String): List<CardDto>

    context(_: Raise<ScryfallError>)
    suspend fun setsRaise(): List<SetDto>

    @Deprecated("Use raise variant", replaceWith = ReplaceWith("cardNamedRaise(name)"))
    suspend fun cardNamed(name: String): Either<String, CardDto>

    @Deprecated("Use raise variant", replaceWith = ReplaceWith("searchCardRaise(searchParam)"))
    suspend fun searchCard(searchParam: String): Either<String, List<CardDto>>

    @Deprecated("Use raise variant", replaceWith = ReplaceWith("setsRaise()"))
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

    // TODO handle exceptions/responses that dont fit ScryfallError
    context(_: Raise<ScryfallError>)
    private suspend inline fun <reified T> HttpResponse.bodyOrError(): T {
        return if (status.isSuccess()) {
            body<T>()
        } else {
            raise(body<ScryfallError>())
        }
    }

    /**
     * Gets a single card object from the search or raises a [ScryfallError]
     * If the error response doesnt fit [ScryfallError] throws an exception
     */
    context(_: Raise<ScryfallError>)
    override suspend fun cardNamedRaise(name: String): CardDto {
        return client.get {
            scryfall("$CardApiBase$FindNamed")
            parameter("fuzzy", name)
        }.bodyOrError()
    }


    // This NEEDS to be called or it will hold up the couroutine scope of suspend fun main
    override fun close() {
        client.close()
    }

    context(_: Raise<ScryfallError>)
    override suspend fun searchCardRaise(searchParam: String): List<CardDto> = client.get {
        scryfall("$CardApiBase$Search")
        parameter("q", searchParam)
    }.bodyOrError<ListResp<CardDto>>().data

    context(_: Raise<ScryfallError>)
    override suspend fun setsRaise(): List<SetDto> =
        client.get { scryfall("sets") }.bodyOrError<ListResp<SetDto>>().data

    @Deprecated("Use raise variant", replaceWith = ReplaceWith("searchCardRaise(searchParam)"))
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

    @Deprecated("Use raise variant", replaceWith = ReplaceWith("setsRaise()"))
    override suspend fun sets(): Either<String, List<SetDto>> = either { withError({ it.details }) { setsRaise() } }

    @Deprecated("Use raise variant", replaceWith = ReplaceWith("cardNamedRaise(name)"))
    override suspend fun cardNamed(name: String): Either<String, CardDto> = either {
        withError({ it.details }) { cardNamedRaise(name) }
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
