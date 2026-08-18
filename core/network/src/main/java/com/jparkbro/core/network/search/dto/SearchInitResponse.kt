package com.jparkbro.core.network.search.dto

import com.jparkbro.core.model.anime.Anime
import kotlinx.serialization.Serializable

@Serializable
data class SearchInitResponse(
    val popularAnimes: List<SearchAnimeResponse> = emptyList(),
)

@Serializable
data class SearchAnimeResponse(
    val animeId: Long,
    val title: String,
    val coverImageUrl: String,
    val isAdult: Boolean = false,
)

fun SearchAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    isAdult = isAdult,
)
