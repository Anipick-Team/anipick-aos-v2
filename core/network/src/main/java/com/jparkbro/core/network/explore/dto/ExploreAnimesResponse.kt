package com.jparkbro.core.network.explore.dto

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

/**
 * `GET /explore/animes` 응답. sort가 "popularity"면 cursor에 [CursorResponse.lastValue]가 없고,
 * "rating"이면 있다 - 둘 다 [CursorResponse]가 optional이라 그대로 처리된다.
 */
@Serializable
data class ExploreAnimesResponse(
    val count: Int,
    val cursor: CursorResponse = CursorResponse(),
    val animes: List<ExploreAnimeResponse> = emptyList(),
)

@Serializable
data class ExploreAnimeResponse(
    val animeId: Long,
    val title: String,
    val coverImageUrl: String,
    val isAdult: Boolean = false,
)

fun ExploreAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    isAdult = isAdult,
)
