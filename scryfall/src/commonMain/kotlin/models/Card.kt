package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@JsExport
@Serializable
data class Card(
    val id: String,
    @SerialName("scryfall_uri")
    val scryfallUrl: String,
    @SerialName("uri")
    val apiUri: String,
    @SerialName("image_uris")
    val imageUris: ImageUris? = null,
)

@JsExport
@Serializable
data class ImageUris(
    val small: String,
    val normal: String,
    val large: String,
    val png: String,
    @SerialName("art_crop")
    val artCrop: String,
    @SerialName("border_crop")
    val borderCrop: String,
)
