package com.jparkbro.core.model.anime

data class RecommendationResult(
    val referenceAnimeTitle: String? = null,
    val animes: List<Anime> = emptyList(),
)
