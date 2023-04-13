package com.mfriend.scryfall.db

import com.mfriend.client.defaultJson
import com.mfriend.models.CardDto
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray

fun cacheDb(data: String):List<CardDto> {
    val jsonData: List<CardDto> = try {
        defaultJson.decodeFromString(data)
    } catch (e: Exception) {
        println(e)
        throw e
    }
    return jsonData
}