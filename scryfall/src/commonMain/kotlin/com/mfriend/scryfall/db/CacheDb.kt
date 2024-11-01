package com.mfriend.scryfall.db

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.mfriend.client.ScryfallApi
import com.mfriend.models.CardDto
import com.mfriend.models.SetDto
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.first

class CacheDb(sqlDriver: SqlDriver) : ScryfallApi {
    private val database = ScryfallCache(sqlDriver)

    suspend fun insertCard(card: CachedCard) {
        database.scryfallCardQueries.insert(card)
    }

    suspend fun findCardByName(name: String): List<CachedCard> {
        return database.scryfallCardQueries.searchByName(name)
            .asFlow()
            .mapToList()
            .first()
    }

    override suspend fun cardNamed(name: String): Either<String, CardDto> {
        TODO()
//        return findCardByName(name).firstOrNull().rightIfNotNull { "Didnt find" }
    }

    override suspend fun searchCard(searchParam: String): Either<String, List<CardDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun sets(): Either<String, List<SetDto>> {
        TODO("Not yet implemented")
    }

}