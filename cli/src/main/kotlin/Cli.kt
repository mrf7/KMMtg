import client.ScryfallApiImpl

suspend fun main() {
    val client = ScryfallApiImpl()
    while (true) {
        println("1) Card Named\n2) Search String\n3) Exit\n")
        val selection = readln().toInt()
        println("input: ")
        val result = when(selection) {
            1 -> client.cardNamed(readln())
            2 -> client.searchCard(readln())
            else -> return
        }
        println(result)
    }
}