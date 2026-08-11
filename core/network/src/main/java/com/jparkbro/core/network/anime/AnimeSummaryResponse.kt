package com.jparkbro.core.network.anime

import com.jparkbro.core.model.anime.Anime
import kotlinx.serialization.Serializable

/**
 * 홈 화면 "추천"([RecommendationAnimesResponse.animes])과 "방영예정"([UpcomingSeasonAnimesResponse.animes])
 * 목록의 애니메 카드 하나. [TrendingAnimeResponse]와 달리 순위 정보(rank)는 없다.
 */
@Serializable
data class AnimeSummaryResponse(
    val animeId: Long? = null,
    val title: String,
    val coverImageUrl: String,
    val isAdult: Boolean = false,
)

fun AnimeSummaryResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    isAdult = isAdult,
)
