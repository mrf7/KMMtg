import client.ScryfallApiImpl
import javax.swing.text.html.HTML.Tag.P

suspend fun main() {
    val client = ScryfallApiImpl()
    println(client.searchCard("chatterfang"))
}