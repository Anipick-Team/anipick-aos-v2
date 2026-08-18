package com.jparkbro.core.network.recommendation.dto

import com.jparkbro.core.network.anime.dto.AnimeSummaryResponse
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

@Serializable
data class RecommendationAnimesDetailResponse(
    val referenceAnimeTitle: String? = null,
    val cursor: CursorResponse = CursorResponse(),
    val animes: List<AnimeSummaryResponse> = emptyList(),
)
