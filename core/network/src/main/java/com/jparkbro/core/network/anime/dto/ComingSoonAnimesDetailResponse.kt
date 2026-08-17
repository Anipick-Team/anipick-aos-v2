package com.jparkbro.core.network.anime.dto

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

/**
 * Home Detail "공개예정" 화면 응답. `GET /animes/coming-soon`. [ComingSoonAnimeResponse](홈 미리보기)와
 * 달리 커서 페이지네이션([cursor])과 전체 개수([count])가 붙어 있다.
 */
@Serializable
data class ComingSoonAnimesDetailResponse(
    val count: Int,
    val cursor: CursorResponse = CursorResponse(),
    val animes: List<ComingSoonAnimeDetailResponse> = emptyList(),
)

@Serializable
data class ComingSoonAnimeDetailResponse(
    val animeId: Long? = null,
    val title: String,
    val coverImageUrl: String,
    val releaseDate: String? = null,
    val isAdult: Boolean = false,
)

fun ComingSoonAnimeDetailResponse.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    subtitle = releaseDate,
    isAdult = isAdult,
)
