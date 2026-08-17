package com.jparkbro.core.network.anime

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.anime.dto.ComingSoonAnimeResponse
import com.jparkbro.core.network.anime.dto.ComingSoonAnimesDetailResponse
import com.jparkbro.core.network.anime.dto.ComingSoonAnimesRequest
import com.jparkbro.core.network.anime.dto.PreferenceSetupSearchRequest
import com.jparkbro.core.network.anime.dto.PreferenceSetupSearchResponse
import com.jparkbro.core.network.anime.dto.RecommendationAnimesDetailResponse
import com.jparkbro.core.network.anime.dto.RecommendationAnimesRequest
import com.jparkbro.core.network.anime.dto.RecommendationAnimesResponse
import com.jparkbro.core.network.anime.dto.TrendingAnimeResponse
import com.jparkbro.core.network.anime.dto.UpcomingSeasonAnimesResponse
import com.jparkbro.core.network.anime.dto.WeeklyAnimeResponse
import com.jparkbro.core.network.get
import io.ktor.client.HttpClient

class KtorAnimeNetworkDataSource(
    private val httpClient: HttpClient,
) : AnimeNetworkDataSource {

    override suspend fun searchPreferenceSetupAnimes(
        request: PreferenceSetupSearchRequest,
    ): Result<PreferenceSetupSearchResponse, DataError.Network> {
        return httpClient.get(
            route = "/explore-search",
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

    override suspend fun getRecommendationAnimesDetail(
        request: RecommendationAnimesRequest,
    ): Result<RecommendationAnimesDetailResponse, DataError.Network> {
        return httpClient.get(
            route = "/recommendation/animes",
            queryParameters = mapOf(
                "lastId" to request.lastId,
                "lastValue" to request.lastValue,
                "size" to request.size,
            ),
        )
    }

    override suspend fun getRecentAnimeRecommendationsDetail(
        animeId: Long,
        request: RecommendationAnimesRequest,
    ): Result<RecommendationAnimesDetailResponse, DataError.Network> {
        return httpClient.get(
            route = "/recommendation/animes/$animeId/recent",
            queryParameters = mapOf(
                "lastId" to request.lastId,
                "lastValue" to request.lastValue,
                "size" to request.size,
            ),
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

    override suspend fun getComingSoonAnimesDetail(
        request: ComingSoonAnimesRequest,
    ): Result<ComingSoonAnimesDetailResponse, DataError.Network> {
        return httpClient.get(
            route = "/animes/coming-soon",
            queryParameters = mapOf(
                "sort" to request.sort,
                "lastId" to request.lastId,
                "lastValue" to request.lastValue,
                "size" to request.size,
            ),
        )
    }
}
