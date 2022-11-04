package models

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseUris(
    val cardmarket: String,
    val tcgplayer: String
)