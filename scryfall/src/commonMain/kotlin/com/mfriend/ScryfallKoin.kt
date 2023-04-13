package com.mfriend

import com.mfriend.client.ScryfallApi
import com.mfriend.client.ScryfallApiImpl
import com.mfriend.scryfall.db.CacheDb
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val scryfallModule: Module = module {
    singleOf(::ScryfallApiImpl) { bind<ScryfallApi>() }
    single {
       CacheDb(get())
    }
}
