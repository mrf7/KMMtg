package client

import kotlinx.serialization.json.Json

val defaultJson = Json {
    ignoreUnknownKeys = true
}