package client

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import models.CardDto
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

interface ScryfallApi {
    suspend fun cardNamed(name: String): Either<String, CardDto>
    suspend fun searchCard(searchParam: String): Either<String, List<CardDto>>
}

suspend inline fun <reified T> HttpResponse.toEither(): Either<String, T> =
    when (this.status.value) {
        in 200..299 -> this.body<T>().right()
        else -> this.body<String>().left()
    }

class ScryfallApiImpl : ScryfallApi {
    private val client = newKtorClient()
    override suspend fun cardNamed(name: String): Either<String, CardDto> {
        val response = client.get {
            scryfall("$CardApiBase$FindNamed")
            parameter("fuzzy", name)
        }

        return response.toEither()
    }

    override suspend fun searchCard(searchParam: String): Either<String, List<CardDto>> {
        val resp: HttpResponse = client.get {
            scryfall("$CardApiBase$Search")
            parameter("q", searchParam)
        }

        return resp
            .toEither<JsonObject>()
            .map { jsonObject -> jsonObject["data"].let { defaultJson.decodeFromJsonElement(it!!) } }
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