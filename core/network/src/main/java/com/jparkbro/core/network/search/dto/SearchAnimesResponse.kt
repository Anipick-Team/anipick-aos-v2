package com.jparkbro.core.network.search.dto

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

@Serializable
data class SearchAnimesResponse(
    val count: Int? = null,
    val nextPage: Long? = null,
    val personCount: Int? = null,
    val studioCount: Int? = null,
    val cursor: CursorResponse? = null,
    val animes: List<SearchResultAnimeResponse>? = null,
)

@Serializable
data class SearchResultAnimeResponse(
    val animeId: Long,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val clickLog: String? = null,
    val impressionLog: String? = null,
    val isAdult: Boolean? = null,
)

fun SearchResultAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    clickLog = clickLog,
    impressionLog = impressionLog,
    isAdult = isAdult,
)
