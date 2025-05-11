package models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


// https://scryfall.com/docs/api/errors
@Serializable
@SerialName("error")
data class ScryfallError(
    // String version of status code
    @SerialName("code")
    val code: String,
    // Http status code
    @SerialName("status")
    val status: Int,
    // Human-readable message
    @SerialName("details")
    val details: String,
    // Non fatal warnings with the response
    @SerialName("warnings")
    val warnings: List<String>,
    // This is a type param, can be used for polymorphic serialization
    // Looks like options are: error, set, list, card, ruling
    @SerialName("object")
    val responseType: String,
    val type: String? = null,
)
