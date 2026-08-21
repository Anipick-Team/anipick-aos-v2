package com.jparkbro.core.network.user.dto

import com.jparkbro.core.model.review.Review
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

/** 마이페이지 "평가한 애니" 목록 응답 */
@Serializable
data class RatedAnimesResponse(
    val count: Int? = null,
    val cursor: CursorResponse? = null,
    val reviews: List<RatedAnimeResponse>? = null,
)

@Serializable
data class RatedAnimeResponse(
    val animeId: Long? = null,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val rating: Float? = null,
    val reviewId: Long? = null,
    val reviewContent: String? = null,
    val createdAt: String? = null,
    val likeCount: Int? = null,
    val isLiked: Boolean? = null,
    val isAdult: Boolean? = null,
)

fun RatedAnimeResponse.toReview(): Review = Review(
    reviewId = reviewId,
    animeId = animeId,
    animeTitle = title,
    animeCoverImageUrl = coverImageUrl,
    content = reviewContent,
    createdAt = createdAt,
    rating = rating,
    likeCount = likeCount,
    isLiked = isLiked,
    isAdult = isAdult,
    isMine = true,
)
