package com.jparkbro.core.network.home.dto

import com.jparkbro.core.model.anime.Anime
import kotlinx.serialization.Serializable

@Serializable
data class WeeklyAnimeResponse(
    val animeId: Long? = null,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val isAdult: Boolean? = null,
)

fun WeeklyAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    isAdult = isAdult,
)
