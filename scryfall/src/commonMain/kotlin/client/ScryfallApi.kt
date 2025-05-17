package client

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.catch
import arrow.core.raise.either
import arrow.core.raise.withError
import ensure
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import io.ktor.http.takeFrom
import io.ktor.serialization.ContentConvertException
import models.CardDto
import models.InvalidResponse
import models.ListResp
import models.ScryfallError
import models.ScryfallErrorResponse
import models.SetDto
import raise

interface ScryfallApi : AutoCloseable {
    context(_: Raise<ScryfallError>)
    suspend fun cardNamedRaise(name: String): CardDto

    context(_: Raise<ScryfallError>)
    suspend fun searchCardRaise(searchParam: String): List<CardDto>

    context(_: Raise<ScryfallError>)
    suspend fun setsRaise(): List<SetDto>

    @Deprecated("Use raise variant", replaceWith = ReplaceWith("searchCardRaise(searchParam)"))
    suspend fun searchCard(searchParam: String): Either<String, List<CardDto>>
}

class ScryfallApiImpl : ScryfallApi, AutoCloseable {
    private val client = newKtorClient()

    context(_: Raise<ScryfallError>)
    private suspend inline fun <reified T> HttpResponse.responseBodyOrRaise(): T {
        // make sure we got a successful response, otherwise raise an error
        ensure(status.isSuccess()) {
            bodyOrError<ScryfallErrorResponse>()
        }
        return bodyOrError<T>()
    }

    context(_: Raise<InvalidResponse>)
    private suspend inline fun <reified T> HttpResponse.bodyOrError(): T =
        catch({ body<T>() }) { t: ContentConvertException ->
            raise(InvalidResponse(body<String>(), T::class.simpleName ?: "unknown class", t))
        }

    /**
     * Gets a single card object from the search or raises a [ScryfallErrorResponse]
     * If the error response doesnt fit [ScryfallErrorResponse] throws an exception
     */
    context(_: Raise<ScryfallError>) override suspend fun cardNamedRaise(name: String): CardDto {
        return client.get {
            scryfall("$CardApiBase$FindNamed")
            parameter("fuzzy", name)
        }.responseBodyOrRaise()
    }


    // This NEEDS to be called or it will hold up the couroutine scope of suspend fun main
    override fun close() {
        client.close()
    }

    context(_: Raise<ScryfallError>)
    override suspend fun searchCardRaise(searchParam: String): List<CardDto> =
        client.get {
            scryfall("$CardApiBase$Search")
            parameter("q", searchParam)
        }.responseBodyOrRaise<ListResp<CardDto>>().data

    context(_: Raise<ScryfallError>)
    override suspend fun setsRaise(): List<SetDto> =
        client.get { scryfall("sets") }.responseBodyOrRaise<ListResp<SetDto>>().data

    @Deprecated("Use raise variant", replaceWith = ReplaceWith("searchCardRaise(searchParam)"))
    override suspend fun searchCard(searchParam: String): Either<String, List<CardDto>> = either {
        withError({ it.toString() }) { searchCardRaise(searchParam) }
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
