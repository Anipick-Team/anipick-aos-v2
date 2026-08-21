package com.jparkbro.core.network.anime.dto

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

/** 홈 방영예정 더보기 화면 응답 */
@Serializable
data class ComingSoonAnimesDetailResponse(
    val count: Int? = null,
    val cursor: CursorResponse? = null,
    val animes: List<ComingSoonAnimeDetailResponse>? = null,
)

@Serializable
data class ComingSoonAnimeDetailResponse(
    val animeId: Long? = null,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val releaseDate: String? = null,
    val isAdult: Boolean? = null,
)

fun ComingSoonAnimeDetailResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    subtitle = releaseDate,
    isAdult = isAdult,
)
