package com.jparkbro.catalog.impl.di

import com.jparkbro.catalog.impl.actor.CatalogActorViewModel
import com.jparkbro.catalog.impl.anime.CatalogAnimeViewModel
import com.jparkbro.catalog.impl.character.CatalogCharacterViewModel
import com.jparkbro.catalog.impl.recommendation.CatalogRecommendationViewModel
import com.jparkbro.catalog.impl.series.CatalogSeriesViewModel
import com.jparkbro.catalog.impl.studio.CatalogStudioViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val catalogModule = module {
    viewModel { params ->
        CatalogAnimeViewModel(
            animeId = params.get(),
            animeRepository = get(),
            reviewRepository = get(),
            ratingRepository = get(),
            communityRepository = get(),
            userRepository = get(),
            globalSnackbarManager = get(),
        )
    }
    viewModel { params ->
        CatalogActorViewModel(
            personId = params.get(),
            actorRepository = get(),
            globalSnackbarManager = get(),
        )
    }
    viewModel { params -> CatalogCharacterViewModel(animeId = params.get(), characterRepository = get()) }
    viewModel { params -> CatalogStudioViewModel(studioId = params.get(), studioRepository = get()) }
    viewModel { params -> CatalogSeriesViewModel(animeId = params.get(), animeTitle = params.get(), seriesRepository = get()) }
    viewModel { params -> CatalogRecommendationViewModel(basedOnAnimeId = params.get(), recommendationRepository = get()) }
}
