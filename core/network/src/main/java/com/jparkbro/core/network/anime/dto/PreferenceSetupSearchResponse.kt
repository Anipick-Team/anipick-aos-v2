package com.jparkbro.core.network.anime.dto

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

/**
 * 홈 화면이 아니라 온보딩의 "취향 설정" 화면 응답. 좋아하는 애니를 검색해서 고르는 화면이라
 * 커서 기반 페이지네이션([cursor])이 붙어 있다.
 */
@Serializable
data class PreferenceSetupSearchResponse(
    val count: Int,
    val cursor: CursorResponse,
    val animes: List<PreferenceSetupAnimeResponse> = emptyList(),
)

@Serializable
data class PreferenceSetupAnimeResponse(
    val animeId: Long,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val genres: List<String?> = emptyList(),
    val isAdult: Boolean? = null,
)

fun PreferenceSetupAnimeResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title ?: "",
    coverImageUrl = coverImageUrl.orEmpty(),
    genres = genres.filterNotNull(),
    isAdult = isAdult ?: false,
)
