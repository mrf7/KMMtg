import com.mfriend.db.MTGDb
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import commfrienddb.Card
import kotlinx.coroutines.flow.Flow

class DatabaseHelper(sqlDriver: SqlDriver) {
    private val database = MTGDb(sqlDriver)
    suspend fun insertCard(card: Card) {
        database.cardQueries.insertCard(card)
    }
    fun getCards(): Flow<List<Card>> {
        return database.cardQueries.selectAll().asFlow().mapToList()
    }
}
