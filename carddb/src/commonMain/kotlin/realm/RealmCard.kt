package realm

import commfrienddb.Card
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class RealmCard(
) : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var set_code: String = ""
    var set_name: String = ""
    var scryfall_url: String = ""
    var image_url: String? = null
    var price: Double? = null

    constructor(
        name: String,
        set_code: String,
        set_name: String,
        scryfall_url: String,
        image_url: String? = null,
        price: Double? = null
    ) : this() {
        this.name = name
        this.set_code = set_code
        this.set_name = set_name
        this.scryfall_url = scryfall_url
        this.image_url = image_url
        this.price = price
    }
}

internal fun Card.toRealm() = RealmCard(name, set_code, set_name, scryfall_url, image_url, price)
internal fun RealmCard.toCard() = Card(name, set_code, set_name, image_url, scryfall_url, price)