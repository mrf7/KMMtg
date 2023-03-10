package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ListResp<out T>(
    val data: List<T>,
    @SerialName("has_more")
    val hasMore: Boolean,
    @SerialName("next_page")
    val nextPage: String? = null,
    val warnings: List<String>? = null
)
