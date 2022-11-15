package com.mfriend.collection

import kotlin.js.JsExport

// Imports a card castle export of cards and returns some stuff i guess
fun importCards() {
    TODO()
}

fun main() {
    println(Foo("bozo"))
}

data class Foo(
    override val configs: String?,
) : IFoo{
    override fun bingBong() {

    }
}

@JsExport
interface IFoo {
    val configs: String?
    fun bingBong()
}