package com.jparkbro.core.datastore.di

import com.jparkbro.core.common.auth.TokenProvider
import com.jparkbro.core.datastore.RecentAnimeDataStore
import com.jparkbro.core.datastore.RecentAnimeDataStoreImpl
import com.jparkbro.core.datastore.RecentSearchDataStore
import com.jparkbro.core.datastore.RecentSearchDataStoreImpl
import com.jparkbro.core.datastore.TokenDataStore
import com.jparkbro.core.datastore.UserDataStore
import com.jparkbro.core.datastore.UserDataStoreImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val datastoreModule = module {
    single<TokenProvider> { TokenDataStore(get()) }
    singleOf(::RecentAnimeDataStoreImpl) { bind<RecentAnimeDataStore>() }
    singleOf(::RecentSearchDataStoreImpl) { bind<RecentSearchDataStore>() }
    singleOf(::UserDataStoreImpl) { bind<UserDataStore>() }
}
