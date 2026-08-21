package com.jparkbro.core.data.recommendation

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.RecommendationResult

/** 추천 관련 데이터를 읽어오는 인터페이스 */
interface RecommendationRepository {
    /** 홈 상세 "오늘의 추천작" 전체 목록 - `GET /recommendation/animes`. */
    suspend fun getRecommendationAnimesDetail(
        lastId: Long? = null,
        lastValue: String? = null,
        size: Long = 18,
    ): Result<RecommendationResult, DataError.Network>
    /** 홈 상세 "최근 본 애니 기반 추천" 전체 목록 - `GET /recommendation/animes/{animeId}/recent`. */
    suspend fun getRecentAnimeRecommendationsDetail(
        animeId: Long,
        lastId: Long? = null,
        lastValue: String? = null,
        size: Long = 18,
    ): Result<RecommendationResult, DataError.Network>

    /** 애니 상세 "이 작품과 비슷한 작품" 전체보기 - `GET /animes/{animeId}/recommendations`. */
    suspend fun getAnimeRecommendations(
        animeId: Long,
        lastId: Long? = null,
        size: Int = 18,
    ): Result<RecommendationResult, DataError.Network>
}
