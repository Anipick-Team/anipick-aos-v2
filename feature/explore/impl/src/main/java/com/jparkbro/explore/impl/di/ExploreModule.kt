package com.jparkbro.explore.impl.di

import com.jparkbro.explore.impl.ExploreViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val exploreModule = module {
    viewModelOf(::ExploreViewModel)
}
