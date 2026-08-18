package com.jparkbro.core.network.home

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.get
import com.jparkbro.core.network.home.dto.ComingSoonAnimeResponse
import com.jparkbro.core.network.home.dto.RecentReviewResponse
import com.jparkbro.core.network.home.dto.RecommendationAnimesResponse
import com.jparkbro.core.network.home.dto.TrendingAnimeResponse
import com.jparkbro.core.network.home.dto.WeeklyAnimeResponse
import io.ktor.client.HttpClient

class KtorHomeNetworkDataSource(
    private val httpClient: HttpClient,
) : HomeNetworkDataSource {

    override suspend fun getTrendingAnimes(): Result<List<TrendingAnimeResponse>, DataError.Network> {
        return httpClient.get(
            route = "/home/animes/trending"
        )
    }

    override suspend fun getWeeklyAnimes(day: String): Result<List<WeeklyAnimeResponse>, DataError.Network> {
        return httpClient.get(
            route = "/home/animes/weekly",
            queryParameters = mapOf("day" to day),
        )
    }

    override suspend fun getRecommendationAnimes(): Result<RecommendationAnimesResponse, DataError.Network> {
        return httpClient.get(
            route = "/home/recommendation/animes"
        )
    }

    override suspend fun getRecentAnimeRecommendations(
        animeId: Long,
    ): Result<RecommendationAnimesResponse, DataError.Network> {
        return httpClient.get(
            route = "/home/recommendation/animes/$animeId/recent"
        )
    }

    override suspend fun getComingSoonAnimes(): Result<List<ComingSoonAnimeResponse>, DataError.Network> {
        return httpClient.get(
            route = "/home/animes/coming-soon"
        )
    }

    override suspend fun getRecentReviews(): Result<List<RecentReviewResponse>, DataError.Network> {
        return httpClient.get(
            route = "/home/reviews/recent"
        )
    }
}
