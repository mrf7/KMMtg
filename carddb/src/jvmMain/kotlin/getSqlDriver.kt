import com.mfriend.db.MTGDb
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

actual fun getSqlDriver(): SqlDriver {
   val driver =  JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
   MTGDb.Schema.create(driver)
   return driver
}