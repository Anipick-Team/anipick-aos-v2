package com.jparkbro.core.network.anime.dto

import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

/** 요일별 애니 목록 응답 */
@Serializable
data class AnimesByDayResponse(
    val count: Int? = null,
    val cursor: CursorResponse? = null,
    val animes: List<AnimeSummaryResponse>? = null,
)
