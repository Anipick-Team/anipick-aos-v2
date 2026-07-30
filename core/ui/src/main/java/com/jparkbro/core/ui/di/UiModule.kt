package com.jparkbro.core.ui.di

import com.jparkbro.core.ui.GlobalSnackbarManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/** core:ui 모듈이 제공하는 Koin DI 모듈. */
val uiModule = module {
    singleOf(::GlobalSnackbarManager)
}
