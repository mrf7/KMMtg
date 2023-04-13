package com.mfriend.scryfall.db

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.first

class CacheDb(sqlDriver: SqlDriver) {
    private val database = ScryfallCache(sqlDriver)

    suspend fun insertCard(card: CachedCard) {
        database.scryfallCardQueries.insert(card)
    }

    suspend fun findCardByName(name: String):List<CachedCard> {
        return database.scryfallCardQueries.searchByName(name)
            .asFlow()
            .mapToList()
            .first()
    }


}