package com.mfriend.collection

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.withError
import client.ScryfallApi
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter

data class CardEntry(val name: String, val count: Int, val set: String, val number: String, val foil: Boolean)

interface CollectionImporter {
    suspend fun parseCardCastle(fileName: String): Either<String, List<CardEntry>>

    suspend fun writeDeckbox(fileName: String, entries: List<CardEntry>)
    fun toEntry(row: Map<String, String>, sets: Map<String, String>): CardEntry
}

class CollectionImporterImpl(private val client: ScryfallApi) : CollectionImporter {
    override suspend fun parseCardCastle(fileName: String): Either<String, List<CardEntry>> = either {
        val setMap = withError({ it.toString() }) { client.setsRaise() }
            .associate { it.name to it.code }

        return@either Either.catch {
            csvReader().openAsync(fileName) {
                return@openAsync readAllWithHeaderAsSequence()
                    .map { toEntry(it, setMap) }
                    .toList()
            }
        }.mapLeft { it.message ?: "oops" }.bind()
    }

    override suspend fun writeDeckbox(fileName: String, entries: List<CardEntry>) {
        csvWriter().openAsync("$fileName.csv") {
            val rows = entries.map { entry ->
                listOf(entry.count, entry.name, entry.set, entry.number, if (entry.foil) "foil" else "")
            }
            writeRow(listOf("Count", "Name", "Edition", "Card Number", "Foil"))
            writeRows(rows)
        }
    }

    override fun toEntry(row: Map<String, String>, sets: Map<String, String>): CardEntry = CardEntry(
        name = row.getValue("Card Name"),
        count = row.getValue("Count").toInt(),
        set = sets[row.getValue("Set Name")]!!,
        number = row.getValue("Collector Number"),
        foil = row.getValue("Foil").toBoolean(),
    )
}
