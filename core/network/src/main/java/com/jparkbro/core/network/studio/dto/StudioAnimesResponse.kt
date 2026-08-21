package com.jparkbro.core.network.studio.dto

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

@Serializable
data class StudioAnimesResponse(
    val studioName: String? = null,
    val cursor: CursorResponse? = null,
    val animes: List<StudioAnimeResponse>? = null,
)

@Serializable
data class StudioAnimeResponse(
    val animeId: Long,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val seasonYear: String? = null,
    val isAdult: Boolean? = null,
)

fun StudioAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    seasonYear = seasonYear,
    isAdult = isAdult,
)
