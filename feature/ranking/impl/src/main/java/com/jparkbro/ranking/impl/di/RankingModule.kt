package com.jparkbro.ranking.impl.di

import com.jparkbro.ranking.impl.RankingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val rankingModule = module {
    viewModelOf(::RankingViewModel)
}
