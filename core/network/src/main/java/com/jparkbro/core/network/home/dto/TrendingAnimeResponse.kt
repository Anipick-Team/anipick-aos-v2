package com.jparkbro.core.network.home.dto

import com.jparkbro.core.model.anime.Anime
import kotlinx.serialization.Serializable

@Serializable
data class TrendingAnimeResponse(
    val animeId: Long? = null,
    val title: String,
    val rank: Int? = null,
    val coverImageUrl: String,
    val isAdult: Boolean = false,
)

fun TrendingAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    rank = rank,
    coverImageUrl = coverImageUrl,
    isAdult = isAdult,
)