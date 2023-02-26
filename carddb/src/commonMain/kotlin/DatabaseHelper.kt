import commfrienddb.Card
import kotlinx.coroutines.flow.Flow

interface DatabaseHelper {
    suspend fun insertCard(card: Card)
    fun getCardsFlow(): Flow<List<Card>>
    suspend fun getCards(): List<Card>
}