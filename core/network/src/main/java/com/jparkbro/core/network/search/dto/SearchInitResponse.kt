package com.jparkbro.core.network.search.dto

import com.jparkbro.core.model.anime.Anime
import kotlinx.serialization.Serializable

@Serializable
data class SearchInitResponse(
    val popularAnimes: List<SearchAnimeResponse>? = null,
)

@Serializable
data class SearchAnimeResponse(
    val animeId: Long,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val isAdult: Boolean? = null,
)

fun SearchAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    isAdult = isAdult,
)
