package com.jparkbro.core.network.anime.dto

import com.jparkbro.core.model.anime.Anime
import kotlinx.serialization.Serializable

/** 홈 추천/방영예정 목록의 애니 카드 (순위 정보 없음) */
@Serializable
data class AnimeSummaryResponse(
    val animeId: Long? = null,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val isAdult: Boolean? = null,
)

fun AnimeSummaryResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    isAdult = isAdult,
)
