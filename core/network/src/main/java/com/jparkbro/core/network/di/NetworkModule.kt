package com.jparkbro.core.network.di

import com.jparkbro.core.network.HttpClientFactory
import com.jparkbro.core.network.anime.AnimeNetworkDataSource
import com.jparkbro.core.network.anime.KtorAnimeNetworkDataSource
import com.jparkbro.core.network.auth.AuthNetworkDataSource
import com.jparkbro.core.network.auth.KtorAuthNetworkDataSource
import com.jparkbro.core.network.common.CommonNetworkDataSource
import com.jparkbro.core.network.common.KtorCommonNetworkDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/** core:network 모듈이 제공하는 Koin DI 모듈. HttpClient는 앱 전역에서 하나만 재사용하도록 싱글턴으로 등록. */
val networkModule = module {
    single { HttpClientFactory(get()).build() }

    singleOf(::KtorAnimeNetworkDataSource).bind<AnimeNetworkDataSource>()
    singleOf(::KtorAuthNetworkDataSource).bind<AuthNetworkDataSource>()
    singleOf(::KtorCommonNetworkDataSource).bind<CommonNetworkDataSource>()
}