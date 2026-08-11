package com.jparkbro.core.network.anime

import kotlinx.serialization.Serializable

/**
 * 홈 화면 "추천" 섹션 응답. `GET /home/recommendation/animes`(전체 추천) 또는
 * `GET /home/recommendation/animes/{animeId}/recent`(최근 본 애니 기준 추천) 둘 다 이 모양으로 내려온다.
 * [referenceAnimeTitle]은 후자처럼 특정 애니를 기준으로 추천한 경우에만 채워지고,
 * 기준 애니가 없는 일반 추천이면 null.
 */
@Serializable
data class RecommendationAnimesResponse(
    val referenceAnimeTitle: String? = null,
    val animes: List<AnimeSummaryResponse> = emptyList(),
)
