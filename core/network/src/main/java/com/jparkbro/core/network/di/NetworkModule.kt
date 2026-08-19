package com.jparkbro.core.network.di

import com.jparkbro.core.network.HttpClientFactory
import com.jparkbro.core.network.actor.ActorNetworkDataSource
import com.jparkbro.core.network.actor.KtorActorNetworkDataSource
import com.jparkbro.core.network.anime.AnimeNetworkDataSource
import com.jparkbro.core.network.anime.KtorAnimeNetworkDataSource
import com.jparkbro.core.network.auth.AuthNetworkDataSource
import com.jparkbro.core.network.auth.KtorAuthNetworkDataSource
import com.jparkbro.core.network.character.CharacterNetworkDataSource
import com.jparkbro.core.network.character.KtorCharacterNetworkDataSource
import com.jparkbro.core.network.common.CommonNetworkDataSource
import com.jparkbro.core.network.common.KtorCommonNetworkDataSource
import com.jparkbro.core.network.explore.ExploreNetworkDataSource
import com.jparkbro.core.network.explore.KtorExploreNetworkDataSource
import com.jparkbro.core.network.home.HomeNetworkDataSource
import com.jparkbro.core.network.home.KtorHomeNetworkDataSource
import com.jparkbro.core.network.image.ImageNetworkDataSource
import com.jparkbro.core.network.image.KtorImageNetworkDataSource
import com.jparkbro.core.network.ranking.KtorRankingNetworkDataSource
import com.jparkbro.core.network.ranking.RankingNetworkDataSource
import com.jparkbro.core.network.recommendation.KtorRecommendationNetworkDataSource
import com.jparkbro.core.network.recommendation.RecommendationNetworkDataSource
import com.jparkbro.core.network.review.KtorReviewNetworkDataSource
import com.jparkbro.core.network.review.ReviewNetworkDataSource
import com.jparkbro.core.network.search.KtorSearchNetworkDataSource
import com.jparkbro.core.network.search.SearchNetworkDataSource
import com.jparkbro.core.network.series.KtorSeriesNetworkDataSource
import com.jparkbro.core.network.series.SeriesNetworkDataSource
import com.jparkbro.core.network.studio.KtorStudioNetworkDataSource
import com.jparkbro.core.network.studio.StudioNetworkDataSource
import com.jparkbro.core.network.user.KtorUserNetworkDataSource
import com.jparkbro.core.network.user.UserNetworkDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/** core:network 모듈이 제공하는 Koin DI 모듈. HttpClient는 앱 전역에서 하나만 재사용하도록 싱글턴으로 등록. */
val networkModule = module {
    single { HttpClientFactory(get()).build() }

    singleOf(::KtorActorNetworkDataSource).bind<ActorNetworkDataSource>()
    singleOf(::KtorAnimeNetworkDataSource).bind<AnimeNetworkDataSource>()
    singleOf(::KtorAuthNetworkDataSource).bind<AuthNetworkDataSource>()
    singleOf(::KtorCharacterNetworkDataSource).bind<CharacterNetworkDataSource>()
    singleOf(::KtorCommonNetworkDataSource).bind<CommonNetworkDataSource>()
    singleOf(::KtorExploreNetworkDataSource).bind<ExploreNetworkDataSource>()
    singleOf(::KtorHomeNetworkDataSource).bind<HomeNetworkDataSource>()
    singleOf(::KtorImageNetworkDataSource).bind<ImageNetworkDataSource>()
    singleOf(::KtorRankingNetworkDataSource).bind<RankingNetworkDataSource>()
    singleOf(::KtorRecommendationNetworkDataSource).bind<RecommendationNetworkDataSource>()
    singleOf(::KtorReviewNetworkDataSource).bind<ReviewNetworkDataSource>()
    singleOf(::KtorSearchNetworkDataSource).bind<SearchNetworkDataSource>()
    singleOf(::KtorSeriesNetworkDataSource).bind<SeriesNetworkDataSource>()
    singleOf(::KtorStudioNetworkDataSource).bind<StudioNetworkDataSource>()
    singleOf(::KtorUserNetworkDataSource).bind<UserNetworkDataSource>()
}