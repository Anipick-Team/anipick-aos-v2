package com.jparkbro.core.network.anime

import com.jparkbro.core.model.anime.Anime
import kotlinx.serialization.Serializable

/** 홈 화면 "실시간 인기" 섹션 응답. `GET /home/animes/trending` — 랭킹 순으로 [rank]가 매겨진다. */
@Serializable
data class TrendingAnimeResponse(
    val animeId: Long? = null,
    val title: String,
    val rank: Int? = null,
    val coverImageUrl: String,
    val isAdult: Boolean = false,
)

fun TrendingAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    rank = rank,
    coverImageUrl = coverImageUrl,
    isAdult = isAdult,
)