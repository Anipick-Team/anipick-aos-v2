package com.jparkbro.core.network.di

import com.jparkbro.core.network.HttpClientFactory
import org.koin.dsl.module

/** core:network 모듈이 제공하는 Koin DI 모듈. HttpClient는 앱 전역에서 하나만 재사용하도록 싱글턴으로 등록. */
val networkModule = module {
    single { HttpClientFactory().build() }

//    singleOf(::KtorUserNetworkDataSource).bind<UserNetworkDataSource>()
}