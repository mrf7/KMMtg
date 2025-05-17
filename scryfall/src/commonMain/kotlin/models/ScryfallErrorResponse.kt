package models

import io.ktor.serialization.ContentConvertException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sealed error interface of all recoverable errors from Scryfall API
 */
sealed interface ScryfallError

/**
 * Structured error response body from Scryfall API
 * Use [details] for a human-readable message
 * https://scryfall.com/docs/api/errors
 */
@Serializable
@SerialName("error")
data class ScryfallErrorResponse(
    /**
     * String version of status code
     */
    @SerialName("code")
    val code: String,
    /**
     *  Http status code
     */
    @SerialName("status")
    val status: Int,
    /**
     * Human-readable message
     */
    @SerialName("details")
    val details: String,
    /**
     * Additional, Non-fatal warnings with the response
     */
    @SerialName("warnings")
    val warnings: List<String>? = null,
    /**
     * This is a type param, can be used for polymorphic serialization
     * Looks like options are: error, set, list, card, ruling
     */
    @SerialName("object")
    val responseType: String,
    val type: String? = null,
) : ScryfallError

/**
 * Represents an unxpected resonse body from the Scryfall API which caused a serialization erro
 * [response] is the actual body that was returned
 * [expectedType] is the simple name of the type that was expected
 * [exception] is the exception that was thrown during serialization
 */
data class InvalidResponse(
    val response: String,
    val expectedType: String,
    val exception: ContentConvertException? = null,
) : ScryfallError
