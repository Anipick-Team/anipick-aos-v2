package com.jparkbro.core.data.recommendation

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.model.anime.RecommendationResult
import com.jparkbro.core.network.anime.dto.toAnime
import com.jparkbro.core.network.common.toCursor
import com.jparkbro.core.network.recommendation.RecommendationNetworkDataSource
import com.jparkbro.core.network.recommendation.dto.RecommendationAnimesRequest

class RecommendationRepositoryImpl(
    private val recommendationNetworkDataSource: RecommendationNetworkDataSource,
) : RecommendationRepository {

    override suspend fun getRecommendationAnimesDetail(
        lastId: Long?,
        lastValue: String?,
        size: Long,
    ): Result<RecommendationResult, DataError.Network> {
        val request = RecommendationAnimesRequest(lastId = lastId, lastValue = lastValue, size = size)
        return recommendationNetworkDataSource.getRecommendationAnimesDetail(request).map { response ->
            RecommendationResult(
                referenceAnimeTitle = response.referenceAnimeTitle,
                cursor = response.cursor.toCursor(),
                animes = response.animes.map { it.toAnime() },
            )
        }
    }

    override suspend fun getRecentAnimeRecommendationsDetail(
        animeId: Long,
        lastId: Long?,
        lastValue: String?,
        size: Long,
    ): Result<RecommendationResult, DataError.Network> {
        val request = RecommendationAnimesRequest(lastId = lastId, lastValue = lastValue, size = size)
        return recommendationNetworkDataSource.getRecentAnimeRecommendationsDetail(animeId, request).map { response ->
            RecommendationResult(
                referenceAnimeTitle = response.referenceAnimeTitle,
                cursor = response.cursor.toCursor(),
                animes = response.animes.map { it.toAnime() },
            )
        }
    }

    override suspend fun getAnimeRecommendations(
        animeId: Long,
        lastId: Long?,
        size: Int,
    ): Result<RecommendationResult, DataError.Network> {
        return recommendationNetworkDataSource.getAnimeRecommendations(animeId, lastId, size).map { response ->
            RecommendationResult(
                referenceAnimeTitle = response.referenceAnimeTitle,
                cursor = response.cursor.toCursor(),
                animes = response.animes.map { it.toAnime() },
            )
        }
    }
}
