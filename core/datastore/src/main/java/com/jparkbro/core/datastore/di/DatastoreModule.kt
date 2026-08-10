package com.jparkbro.core.datastore.di

import com.jparkbro.core.common.auth.TokenProvider
import com.jparkbro.core.datastore.RecentAnimeDataStore
import com.jparkbro.core.datastore.TokenDataStore
import org.koin.dsl.module

val datastoreModule = module {
    single<TokenProvider> { TokenDataStore(get()) }
    single { RecentAnimeDataStore(get()) }
}
