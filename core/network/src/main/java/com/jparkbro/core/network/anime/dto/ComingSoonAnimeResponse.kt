package com.jparkbro.core.network.anime.dto

import com.jparkbro.core.model.anime.Anime
import kotlinx.serialization.Serializable

/**
 * 홈 화면 "공개예정" 섹션 응답. `GET /home/animes/coming-soon` — [UpcomingSeasonAnimesResponse]처럼
 * 시즌 단위로 묶이지 않고, 애니별 [releaseDate]로 개별 공개일이 잡혀 있는 목록.
 */
@Serializable
data class ComingSoonAnimeResponse(
    val animeId: Long? = null,
    val title: String,
    val coverImageUrl: String,
    val releaseDate: String? = null,
    val isAdult: Boolean = false,
)

fun ComingSoonAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    subtitle = releaseDate,
    isAdult = isAdult,
)
