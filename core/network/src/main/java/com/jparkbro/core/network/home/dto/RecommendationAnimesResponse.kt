package com.jparkbro.core.network.home.dto

import com.jparkbro.core.network.anime.dto.AnimeSummaryResponse
import kotlinx.serialization.Serializable

@Serializable
data class RecommendationAnimesResponse(
    val referenceAnimeTitle: String? = null,
    val animes: List<AnimeSummaryResponse> = emptyList(),
)
