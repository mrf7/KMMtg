import client.ScryfallApi
import client.ScryfallApiImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val scryfallModule: Module = module {
    singleOf(::ScryfallApiImpl) { bind<ScryfallApi>() }
}