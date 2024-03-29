package com.mfriend.db

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    single<SqlDriver> {
        NativeSqliteDriver(MTGDb.Schema, "mtg.db")
    }
}