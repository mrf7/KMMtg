package com.mfriend.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetDto(
    @SerialName("name")
    val name: String,
    @SerialName("code")
    val code: String,
    @SerialName("scryfall_uri")
    val scryfallUri: String,
    @SerialName("search_uri")
    val searchUri: String,
    @SerialName("id")
    val id: String,
    @SerialName("uri")
    val uri: String
//    @SerialName("digital")
//    val digital: Boolean,
//    @SerialName("foil_only")
//    val foilOnly: Boolean,
//    @SerialName("icon_svg_uri")
//    val iconSvgUri: String,
//    @SerialName("mtgo_code")
//    val mtgoCode: String,
//    @SerialName("nonfoil_only")
//    val nonfoilOnly: Boolean,
//    @SerialName("object")
//    val objectX: String,
//    @SerialName("printed_size")
//    val printedSize: Int,
//    @SerialName("released_at")
//    val releasedAt: String,
//    @SerialName("set_type")
//    val setType: String,
//    @SerialName("tcgplayer_id")
//    val tcgplayerId: Int,
)