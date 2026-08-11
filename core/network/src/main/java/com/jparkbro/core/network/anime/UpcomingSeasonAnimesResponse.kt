package com.jparkbro.core.network.anime

import kotlinx.serialization.Serializable

/**
 * 홈 화면 "방영예정" 섹션 응답. `GET /animes/upcoming-season` — 아직 이번 시즌에 방영을
 * 시작 안 했지만 편성이 예정된 애니 목록. [season]/[seasonYear]는 그 대상 시즌(예: 2025년 4쿨) 정보.
 */
@Serializable
data class UpcomingSeasonAnimesResponse(
    val season: Int? = null,
    val seasonYear: Int? = null,
    val animes: List<AnimeSummaryResponse> = emptyList(),
)
