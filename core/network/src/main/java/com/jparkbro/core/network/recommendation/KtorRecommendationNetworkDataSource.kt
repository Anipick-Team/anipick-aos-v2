package com.jparkbro.core.network.recommendation

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.get
import com.jparkbro.core.network.recommendation.dto.RecommendationAnimesDetailResponse
import com.jparkbro.core.network.recommendation.dto.RecommendationAnimesRequest
import io.ktor.client.HttpClient

class KtorRecommendationNetworkDataSource(
    private val httpClient: HttpClient,
) : RecommendationNetworkDataSource {

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

    override suspend fun getAnimeRecommendations(
        animeId: Long,
        lastId: Long?,
        size: Int,
    ): Result<RecommendationAnimesDetailResponse, DataError.Network> {
        return httpClient.get(
            route = "/animes/$animeId/recommendations",
            queryParameters = mapOf(
                "lastId" to lastId,
                "size" to size,
            ),
        )
    }
}
