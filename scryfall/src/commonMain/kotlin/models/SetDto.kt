package models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://scryfall.com/docs/api/sets
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
    val uri: String,
    @SerialName("digital")
    val digital: Boolean,
    @SerialName("set_type")
    val setType: String,
    @SerialName("arena_code")
    val arenaCode: String? = null
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
//    @SerialName("tcgplayer_id")
//    val tcgplayerId: Int,
)