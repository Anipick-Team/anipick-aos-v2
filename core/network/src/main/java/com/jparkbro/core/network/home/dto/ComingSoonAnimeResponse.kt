package com.jparkbro.core.network.home.dto

import com.jparkbro.core.model.anime.Anime
import kotlinx.serialization.Serializable

@Serializable
data class ComingSoonAnimeResponse(
    val animeId: Long? = null,
    val title: String,
    val coverImageUrl: String,
    val releaseDate: String? = null,
    val isAdult: Boolean = false,
)

fun ComingSoonAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    subtitle = releaseDate,
    isAdult = isAdult,
)
