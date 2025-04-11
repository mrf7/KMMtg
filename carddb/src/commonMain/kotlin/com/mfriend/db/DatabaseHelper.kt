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

    fun insertDeck(name: String) {
        return database.deckQueries.insertDeck(name, null)
    }

    fun getDecks(): List<Deck> {
        return database.deckQueries.selectAllDecks().executeAsList()
    }

    fun getCardsinDeck(deck: Deck) = database.deckCardQueries.getCardsInDeck(deck.deck_id).executeAsList()

    fun insertCard(deck: Deck, card: Card) {
        database.cardQueries.insertCard(card)
        database.deckCardQueries.addCardToDeck(deck.deck_id, card.id, 1)
    }
}
