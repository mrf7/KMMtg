import com.mfriend.db.MTGDb
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

actual fun getSqlDriver(): SqlDriver {
   return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
}