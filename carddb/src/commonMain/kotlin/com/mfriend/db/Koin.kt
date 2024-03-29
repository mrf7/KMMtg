package com.mfriend.db

import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val databaseModule = module {
    includes(platformModule)
    singleOf(::DatabaseHelperImpl) { bind<DatabaseHelper>() }
}
internal expect val platformModule: Module