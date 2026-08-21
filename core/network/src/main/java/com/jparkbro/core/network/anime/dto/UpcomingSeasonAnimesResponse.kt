package com.jparkbro.core.network.anime.dto

import kotlinx.serialization.Serializable

/** 홈 방영예정 섹션 응답 */
@Serializable
data class UpcomingSeasonAnimesResponse(
    val season: Int? = null,
    val seasonYear: Int? = null,
    val animes: List<AnimeSummaryResponse>? = null,
)
