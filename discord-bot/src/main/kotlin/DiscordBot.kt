import client.ScryfallApiImpl

suspend fun main() {
    val client = ScryfallApiImpl()
    println(client.searchCard("Chatterfang"))
}