package com.jparkbro.core.network.anime

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

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
