import com.mfriend.db.MTGDb
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import commfrienddb.Card
import kotlinx.coroutines.flow.Flow

interface DatabaseHelper {
    suspend fun insertCard(card: Card)
    fun getCards(): Flow<List<Card>>
}

class DatabaseHelperImpl(sqlDriver: SqlDriver) : DatabaseHelper {
    private val database = MTGDb(sqlDriver)
    override suspend fun insertCard(card: Card) {
        database.cardQueries.insertCard(card)
    }
    override fun getCards(): Flow<List<Card>> {
        return database.cardQueries.selectAll().asFlow().mapToList()
    }
}
