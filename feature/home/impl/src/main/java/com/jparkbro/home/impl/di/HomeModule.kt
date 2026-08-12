package com.jparkbro.home.impl.di

import com.jparkbro.home.api.HomeDetailType
import com.jparkbro.home.impl.detail.HomeDetailViewModel
import com.jparkbro.home.impl.main.HomeMainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::HomeMainViewModel)
    viewModel { params ->
        HomeDetailViewModel(type = params.get<HomeDetailType>(), animeRepository = get(), reviewRepository = get())
    }
}
