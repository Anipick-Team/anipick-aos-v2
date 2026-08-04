package com.jparkbro.home.impl.di

import com.jparkbro.home.impl.detail.HomeDetailViewModel
import com.jparkbro.home.impl.main.HomeMainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::HomeMainViewModel)
    viewModelOf(::HomeDetailViewModel)
}
