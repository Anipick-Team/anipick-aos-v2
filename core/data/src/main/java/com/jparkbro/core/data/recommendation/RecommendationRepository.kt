package com.jparkbro.core.data.recommendation

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.RecommendationResult

interface RecommendationRepository {
    /** Home Detail "오늘의 추천작" 전체 목록. [lastId]/[lastValue]가 null이면 첫 페이지. */
    suspend fun getRecommendationAnimesDetail(
        lastId: Long? = null,
        lastValue: String? = null,
        size: Long = 18,
    ): Result<RecommendationResult, DataError.Network>
    /** Home Detail "최근 본 애니 기반 추천" 전체 목록. [lastId]/[lastValue]가 null이면 첫 페이지. */
    suspend fun getRecentAnimeRecommendationsDetail(
        animeId: Long,
        lastId: Long? = null,
        lastValue: String? = null,
        size: Long = 18,
    ): Result<RecommendationResult, DataError.Network>

    /** 애니 상세 "이 작품과 비슷한 작품" 추천 전체보기. [lastId]가 null이면 첫 페이지. */
    suspend fun getAnimeRecommendations(
        animeId: Long,
        lastId: Long? = null,
        size: Int = 18,
    ): Result<RecommendationResult, DataError.Network>
}
