package com.jparkbro.core.network.anime

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.get
import io.ktor.client.HttpClient

class KtorAnimeNetworkDataSource(
    private val httpClient: HttpClient,
) : AnimeNetworkDataSource {

    override suspend fun searchPreferenceSetupAnimes(
        request: PreferenceSetupSearchRequest,
    ): Result<PreferenceSetupSearchResponse, DataError.Network> {
        return httpClient.get(
            route = "explore-search",
            queryParameters = mapOf(
                "query" to request.query,
                "year" to request.year,
                "season" to request.season,
                "genres" to request.genres,
                "lastId" to request.lastId,
                "size" to request.size,
            )
        )
    }

    override suspend fun getTrendingAnimes(): Result<List<TrendingAnimeResponse>, DataError.Network> {
        return httpClient.get(
            route = "/home/animes/trending"
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

    override suspend fun getUpcomingSeasonAnimes(): Result<UpcomingSeasonAnimesResponse, DataError.Network> {
        return httpClient.get(
            route = "/animes/upcoming-season"
        )
    }

    override suspend fun getComingSoonAnimes(): Result<List<ComingSoonAnimeResponse>, DataError.Network> {
        return httpClient.get(
            route = "/home/animes/coming-soon"
        )
    }
}
