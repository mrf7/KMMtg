import commfrienddb.Card
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.UpdatedResults
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import realm.RealmCard
import realm.toCard
import realm.toRealm

class RealmDatabase : DatabaseHelper {
    private val config = RealmConfiguration.create(schema = setOf(RealmCard::class))
    val realm = Realm.open(config)
    override suspend fun insertCard(card: Card) {
        realm.write {
            copyToRealm(card.toRealm())
        }
    }

    override fun getCardsFlow(): Flow<List<Card>> {
        val results: RealmResults<RealmCard> = realm.query<RealmCard>().find()
        return results.asFlow().filterIsInstance<UpdatedResults<RealmCard>>().map { it.list.map(RealmCard::toCard) }
    }

    override suspend fun getCards(): List<Card> {
        val results: RealmResults<RealmCard> = realm.query<RealmCard>().find()
        return results.map(RealmCard::toCard)
    }
}