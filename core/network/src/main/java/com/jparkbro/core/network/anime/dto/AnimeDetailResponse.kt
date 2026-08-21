package com.jparkbro.core.network.anime.dto

import com.jparkbro.core.model.anime.AnimeDetail
import com.jparkbro.core.model.metadata.Genre
import com.jparkbro.core.model.studio.Studio
import com.jparkbro.core.network.common.MetadataItemResponse
import kotlinx.serialization.Serializable

@Serializable
data class AnimeDetailResponse(
    val animeId: Long,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val bannerImageUrl: String? = null,
    val description: String? = null,
    val averageRating: String? = null,
    val isLiked: Boolean? = null,
    val watchStatus: String? = null,
    val type: String? = null,
    val reviewCount: Int? = null,
    val genres: List<MetadataItemResponse>? = null,
    val episode: Int? = null,
    val airDate: String? = null,
    val status: String? = null,
    val age: String? = null,
    val studios: List<AnimeDetailStudioResponse>? = null,
    val isAdult: Boolean? = null,
)

@Serializable
data class AnimeDetailStudioResponse(
    val studioId: Long,
    val name: String? = null,
)

fun AnimeDetailResponse.toAnimeDetail(): AnimeDetail = AnimeDetail(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    bannerImageUrl = bannerImageUrl,
    description = description,
    averageRating = averageRating,
    isLiked = isLiked,
    watchStatus = watchStatus,
    type = type,
    reviewCount = reviewCount,
    genres = genres?.map { Genre(id = it.id, name = it.name) },
    episode = episode,
    airDate = airDate,
    status = status,
    age = age,
    studios = studios?.map { Studio(studioId = it.studioId, name = it.name) },
    isAdult = isAdult,
)
