package com.jparkbro.core.network.search.dto

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

@Serializable
data class SearchAnimesResponse(
    val count: Int = 0,
    val nextPage: Long? = null,
    val personCount: Int = 0,
    val studioCount: Int = 0,
    val cursor: CursorResponse = CursorResponse(),
    val animes: List<SearchResultAnimeResponse> = emptyList(),
)

@Serializable
data class SearchResultAnimeResponse(
    val animeId: Long,
    val title: String,
    val coverImageUrl: String,
    val clickLog: String? = null,
    val impressionLog: String? = null,
    val isAdult: Boolean = false,
)

fun SearchResultAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    clickLog = clickLog,
    impressionLog = impressionLog,
    isAdult = isAdult,
)
