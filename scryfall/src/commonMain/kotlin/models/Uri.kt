package models

import kotlinx.serialization.Serializable
import kotlin.js.JsExport
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class Uri(val url: String)