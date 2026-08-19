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

    /** 애니 상세 "이 작품과 비슷한 작품" 추천 전체보기 - `GET /animes/{animeId}/recommendations`. */
    suspend fun getAnimeRecommendations(
        animeId: Long,
        lastId: Long? = null,
        size: Int = 18,
    ): Result<RecommendationAnimesDetailResponse, DataError.Network>
}
