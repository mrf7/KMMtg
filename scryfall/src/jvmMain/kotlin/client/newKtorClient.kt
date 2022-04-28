package com.mfriend.platform.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

actual fun newKtorClient(): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(defaultJson)
        }
    }
}