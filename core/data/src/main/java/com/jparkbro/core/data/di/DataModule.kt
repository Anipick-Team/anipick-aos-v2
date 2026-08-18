package com.jparkbro.core.data.di

import com.jparkbro.core.data.actor.ActorRepository
import com.jparkbro.core.data.actor.ActorRepositoryImpl
import com.jparkbro.core.data.anime.AnimeRepository
import com.jparkbro.core.data.anime.AnimeRepositoryImpl
import com.jparkbro.core.data.auth.AuthRepository
import com.jparkbro.core.data.auth.AuthRepositoryImpl
import com.jparkbro.core.data.common.CommonRepository
import com.jparkbro.core.data.common.CommonRepositoryImpl
import com.jparkbro.core.data.explore.ExploreRepository
import com.jparkbro.core.data.explore.ExploreRepositoryImpl
import com.jparkbro.core.data.home.HomeRepository
import com.jparkbro.core.data.home.HomeRepositoryImpl
import com.jparkbro.core.data.ranking.RankingRepository
import com.jparkbro.core.data.ranking.RankingRepositoryImpl
import com.jparkbro.core.data.recommendation.RecommendationRepository
import com.jparkbro.core.data.recommendation.RecommendationRepositoryImpl
import com.jparkbro.core.data.review.ReviewRepository
import com.jparkbro.core.data.review.ReviewRepositoryImpl
import com.jparkbro.core.data.search.SearchRepository
import com.jparkbro.core.data.search.SearchRepositoryImpl
import com.jparkbro.core.data.studio.StudioRepository
import com.jparkbro.core.data.studio.StudioRepositoryImpl
import com.jparkbro.core.data.user.UserRepository
import com.jparkbro.core.data.user.UserRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::ActorRepositoryImpl) { bind<ActorRepository>() }
    singleOf(::AnimeRepositoryImpl) { bind<AnimeRepository>() }
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
    singleOf(::CommonRepositoryImpl) { bind<CommonRepository>() }
    singleOf(::ExploreRepositoryImpl) { bind<ExploreRepository>() }
    singleOf(::HomeRepositoryImpl) { bind<HomeRepository>() }
    singleOf(::RankingRepositoryImpl) { bind<RankingRepository>() }
    singleOf(::RecommendationRepositoryImpl) { bind<RecommendationRepository>() }
    singleOf(::ReviewRepositoryImpl) { bind<ReviewRepository>() }
    singleOf(::SearchRepositoryImpl) { bind<SearchRepository>() }
    singleOf(::StudioRepositoryImpl) { bind<StudioRepository>() }
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
}