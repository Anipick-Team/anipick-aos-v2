package com.jparkbro.core.network.anime.dto

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

/** 온보딩 취향 설정 애니 검색 응답 */
@Serializable
data class PreferenceSetupSearchResponse(
    val count: Int? = null,
    val cursor: CursorResponse? = null,
    val animes: List<PreferenceSetupAnimeResponse>? = null,
)

@Serializable
data class PreferenceSetupAnimeResponse(
    val animeId: Long,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val genres: List<String?>? = null,
    val isAdult: Boolean? = null,
)

fun PreferenceSetupAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    genres = genres?.filterNotNull(),
    isAdult = isAdult,
)
