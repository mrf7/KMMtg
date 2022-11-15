package com.mfriend.collection

import kotlin.js.JsExport

// Imports a card castle export of cards and returns some stuff i guess
fun importCards() {
    TODO()
}

fun main() {
    println(Foo("bozo"))
}

@JsExport
data class Foo(
    val configs: String?,
)