package com.jparkbro.core.model.anime

data class UpcomingSeasonResult(
    val season: Int? = null,
    val seasonYear: Int? = null,
    val animes: List<Anime>? = null,
)
