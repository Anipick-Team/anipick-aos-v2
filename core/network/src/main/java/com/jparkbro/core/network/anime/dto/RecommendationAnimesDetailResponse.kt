package com.jparkbro.core.network.anime.dto

import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

/**
 * Home Detail의 추천 목록 화면 응답. `GET /recommendation/animes`(전체 추천) 또는
 * `GET /recommendation/animes/{animeId}/recent`(최근 본 애니 기준 추천) 둘 다 이 모양으로 내려온다.
 * [RecommendationAnimesResponse]와 달리 커서 페이지네이션([cursor])이 붙어 있다.
 */
@Serializable
data class RecommendationAnimesDetailResponse(
    val referenceAnimeTitle: String? = null,
    val cursor: CursorResponse = CursorResponse(),
    val animes: List<AnimeSummaryResponse> = emptyList(),
)
