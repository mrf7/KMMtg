import com.mfriend.db.MTGDb
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import commfrienddb.Card
import kotlinx.coroutines.flow.Flow

expect fun getSqlDriver(): SqlDriver

class DatabaseHelper() {
    val database = MTGDb(getSqlDriver())
    suspend fun insertCard(card: Card) {
        database.cardQueries.insertCard(card)
    }
    fun getCards(): Flow<List<Card>> {
        return database.cardQueries.selectAll().asFlow().mapToList()
    }
}
