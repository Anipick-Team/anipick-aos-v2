package com.jparkbro.core.network.explore.dto

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

/** 탐색 화면 애니 목록 응답 */
@Serializable
data class ExploreAnimesResponse(
    val count: Int? = null,
    val cursor: CursorResponse? = null,
    val animes: List<ExploreAnimeResponse>? = null,
)

@Serializable
data class ExploreAnimeResponse(
    val animeId: Long,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val isAdult: Boolean? = null,
)

fun ExploreAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    isAdult = isAdult,
)
