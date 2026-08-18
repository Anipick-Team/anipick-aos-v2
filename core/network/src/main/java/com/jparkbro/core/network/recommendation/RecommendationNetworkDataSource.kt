package com.jparkbro.core.network.recommendation

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.recommendation.dto.RecommendationAnimesDetailResponse
import com.jparkbro.core.network.recommendation.dto.RecommendationAnimesRequest

interface RecommendationNetworkDataSource {
    suspend fun getRecommendationAnimesDetail(
        request: RecommendationAnimesRequest,
    ): Result<RecommendationAnimesDetailResponse, DataError.Network>

    suspend fun getRecentAnimeRecommendationsDetail(
        animeId: Long,
        request: RecommendationAnimesRequest,
    ): Result<RecommendationAnimesDetailResponse, DataError.Network>
}
