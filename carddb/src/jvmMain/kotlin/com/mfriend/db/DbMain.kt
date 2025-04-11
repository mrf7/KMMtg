package com.mfriend.db

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kotlinx.coroutines.flow.toList

suspend fun main() {
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    MTGDb.Schema.create(driver)
    val db = DatabaseHelperImpl(driver)
    db.insertDeck("test")
    val card =
        Card(
            id = 0, // Assuming the ID is auto-generated
            name = "Black Lotus",
            set_code = "LEA",
            set_name = "Limited Edition Alpha",
            image_url = "https://example.com/black_lotus.jpg",
            scryfall_url = "https://scryfall.com/card/lea/1/black-lotus",
        )
    val deck = db.getDecks().first()
    println(deck)
    db.insertCard(deck, card)
    db.insertCard(card)
    println(db.getCards().toList())
    println(db.getCardsinDeck(deck))
}
