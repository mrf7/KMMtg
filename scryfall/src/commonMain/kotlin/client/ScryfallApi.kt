package com.mfriend.platform.client

import com.mfriend.platform.models.Card
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

interface ScryfallApi {
    suspend fun cardNamed(name: String): Card
    suspend fun searchCard(searchParam: String): List<Card>
}

class ScryfallApiImpl : ScryfallApi {
    private val client = newKtorClient()
    override suspend fun cardNamed(name: String): Card {
        val response = client.get {
            scryfall("$CardApiBase$FindNamed")
            parameter("fuzzy", name)
        }
        return response.body()
    }

    override suspend fun searchCard(searchParam: String): List<Card> {
        val resp = client.get {
            scryfall("$CardApiBase$Search")
            parameter("q", searchParam)
        }.also { println(it.body<String>()) }

        return resp.body<JsonObject>()["data"].let { defaultJson.decodeFromJsonElement(it!!)}
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