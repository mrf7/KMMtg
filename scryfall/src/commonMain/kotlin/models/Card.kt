package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
data class Card(
    val name: String,
    @SerialName("oracle_text")
    val oracleText: String,
    val set: String,
    @SerialName("set_name")
    val setName: String,
    val id: String,
    @SerialName("scryfall_uri")
    val scryfallUrl: Uri,
    @SerialName("uri")
    val apiUri: Uri,
    @SerialName("image_uris")
    val imageUris: ImageUris? = null,
    @SerialName("purchase_uris")
    val purchaseUris: PurchaseUris,
)

@Serializable
data class ImageUris(
    val small: Uri,
    val normal: Uri,
    val large: Uri,
    val png: Uri,
    @SerialName("art_crop")
    val artCrop: Uri,
    @SerialName("border_crop")
    val borderCrop: Uri,
)
