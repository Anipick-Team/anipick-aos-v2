package com.jparkbro.core.network.series.dto

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

@Serializable
data class SeriesAnimesResponse(
    val count: Int? = null,
    val cursor: CursorResponse? = null,
    val animes: List<SeriesAnimeResponse>? = null,
)

@Serializable
data class SeriesAnimeResponse(
    val animeId: Long? = null,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val airDate: String? = null,
    val isAdult: Boolean? = null,
)

fun SeriesAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    subtitle = airDate,
    isAdult = isAdult,
)
