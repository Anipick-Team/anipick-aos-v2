package com.jparkbro.search.impl.di

import com.jparkbro.search.impl.detail.SearchDetailViewModel
import com.jparkbro.search.impl.main.SearchMainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val searchModule = module {
    viewModelOf(::SearchMainViewModel)
    viewModel { params ->
        SearchDetailViewModel(
            query = params.get(),
            searchRepository = get(),
        )
    }
}
