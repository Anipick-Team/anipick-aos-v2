package com.jparkbro.core.model.anime

import com.jparkbro.core.model.metadata.Genre
import com.jparkbro.core.model.studio.Studio

/** 애니 상세 "정보" 탭 - `GET /animes/{animeId}/detail/info`. */
data class AnimeDetail(
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
    val genres: List<Genre>? = null,
    val episode: Int? = null,
    val airDate: String? = null,
    val status: String? = null,
    val age: String? = null,
    val studios: List<Studio>? = null,
    val isAdult: Boolean? = null,
)
