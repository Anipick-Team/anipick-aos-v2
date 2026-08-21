package com.jparkbro.core.data.home

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.anime.RecommendationResult
import com.jparkbro.core.model.review.Review
import com.jparkbro.core.network.anime.dto.toAnime
import com.jparkbro.core.network.home.HomeNetworkDataSource
import com.jparkbro.core.network.home.dto.toAnime
import com.jparkbro.core.network.home.dto.toReview

class HomeRepositoryImpl(
    private val homeNetworkDataSource: HomeNetworkDataSource,
) : HomeRepository {

    override suspend fun getTrendingAnimes(): Result<List<Anime>, DataError.Network> {
        return homeNetworkDataSource.getTrendingAnimes().map { responses ->
            responses.map { it.toAnime() }
        }
    }

    override suspend fun getWeeklyAnimes(day: String): Result<List<Anime>, DataError.Network> {
        return homeNetworkDataSource.getWeeklyAnimes(day).map { responses ->
            responses.map { it.toAnime() }
        }
    }

    override suspend fun getRecommendationAnimes(): Result<RecommendationResult, DataError.Network> {
        return homeNetworkDataSource.getRecommendationAnimes().map { response ->
            RecommendationResult(
                referenceAnimeTitle = response.referenceAnimeTitle,
                animes = response.animes?.map { it.toAnime() },
            )
        }
    }

    override suspend fun getRecentAnimeRecommendations(animeId: Long): Result<RecommendationResult, DataError.Network> {
        return homeNetworkDataSource.getRecentAnimeRecommendations(animeId).map { response ->
            RecommendationResult(
                referenceAnimeTitle = response.referenceAnimeTitle,
                animes = response.animes?.map { it.toAnime() },
            )
        }
    }

    override suspend fun getComingSoonAnimes(): Result<List<Anime>, DataError.Network> {
        return homeNetworkDataSource.getComingSoonAnimes().map { responses ->
            responses.map { it.toAnime() }
        }
    }

    override suspend fun getRecentReviews(): Result<List<Review>, DataError.Network> {
        return homeNetworkDataSource.getRecentReviews().map { responses ->
            responses.map { it.toReview() }
        }
    }
}
