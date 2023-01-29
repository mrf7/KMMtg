import com.mfriend.db.MTGDb
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual fun getSqlDriver(): SqlDriver {
    val driver: SqlDriver = NativeSqliteDriver(MTGDb.Schema, "mtgdb")
    MTGDb.Schema.create(driver)
    return driver
}