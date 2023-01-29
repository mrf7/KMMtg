import com.mfriend.db.MTGDb
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    single<SqlDriver> {
        AndroidSqliteDriver(MTGDb.Schema, get(), "mtg.db")
    }
}
