package com.jparkbro.review.impl.di

import com.jparkbro.review.impl.rated.RatedReviewViewModel
import com.jparkbro.review.impl.recent.RecentReviewViewModel
import com.jparkbro.review.impl.write.ReviewWriteViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val reviewModule = module {
    viewModelOf(::RecentReviewViewModel)
    viewModelOf(::ReviewWriteViewModel)
    viewModelOf(::RatedReviewViewModel)
}
