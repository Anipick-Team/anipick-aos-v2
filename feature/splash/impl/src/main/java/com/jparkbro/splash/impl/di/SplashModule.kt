package com.jparkbro.splash.impl.di

import com.jparkbro.splash.impl.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val splashModule = module {
    viewModelOf(::SplashViewModel)
}
