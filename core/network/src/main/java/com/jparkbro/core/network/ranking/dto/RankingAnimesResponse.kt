package com.jparkbro.core.network.ranking.dto

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.anime.RankingTrend
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

@Serializable
data class RankingAnimesResponse(
    val cursor: CursorResponse = CursorResponse(),
    val animes: List<RankingAnimeResponse> = emptyList(),
)

@Serializable
data class RankingAnimeResponse(
    val animeId: Long,
    val title: String,
    val coverImageUrl: String,
    val rank: Int? = null,
    val change: String? = null,
    val trend: String? = null,
    val genres: List<String> = emptyList(),
    val popularity: Int? = null,
    val trending: Int? = null,
    val isAdult: Boolean = false,
)

fun RankingAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    rank = rank,
    change = change,
    trend = trend?.toRankingTrend(),
    genres = genres,
    popularity = popularity,
    trending = trending,
    isAdult = isAdult,
)

private fun String.toRankingTrend(): RankingTrend? = when (this) {
    "up" -> RankingTrend.UP
    "down" -> RankingTrend.DOWN
    "same" -> RankingTrend.SAME
    "new" -> RankingTrend.NEW
    else -> null
}
