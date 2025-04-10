package com.mfriend.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

interface DatabaseHelper {
    suspend fun insertCard(card: Card)
    fun getCards(): Flow<List<Card>>
}

class DatabaseHelperImpl(sqlDriver: SqlDriver) : DatabaseHelper {
    private val database = MTGDb(sqlDriver)
    override suspend fun insertCard(card: Card) {
        database.cardQueries.insertCard(card)
    }

    override fun getCards(): Flow<List<Card>> = database.cardQueries.selectAll().asFlow().mapToList(Dispatchers.Default)
    fun getDeck(id: Long) {
        database.deckQueries.selectDeckById(id).executeAsOneOrNull()?.let {
            println(it)
        }
    }
}
