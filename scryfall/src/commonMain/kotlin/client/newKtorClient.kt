package client

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal fun newKtorClient(logLevel: LogLevel = LogLevel.NONE): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(defaultJson)
    }
    install(Logging) {
        level = logLevel
    }
}

private val defaultJson = Json {
    ignoreUnknownKeys = true
}