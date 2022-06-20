package client

import client.defaultJson
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

fun newKtorClient(): HttpClient =  HttpClient {
    install(ContentNegotiation) {
        json(defaultJson)
    }
}

