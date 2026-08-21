package com.jparkbro.core.network.review.dto

import com.jparkbro.core.model.review.Review
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

@Serializable
data class AnimeReviewsResponse(
    val count: Int? = null,
    val cursor: CursorResponse? = null,
    val reviews: List<AnimeReviewResponse>? = null,
)

@Serializable
data class AnimeReviewResponse(
    val reviewId: Long? = null,
    val userId: Long? = null,
    val nickname: String? = null,
    val profileImageUrl: String? = null,
    val rating: Float? = null,
    val content: String? = null,
    val createdAt: String? = null,
    val isSpoiler: Boolean? = null,
    val likeCount: Int? = null,
    val isLiked: Boolean? = null,
    val isMine: Boolean? = null,
)

fun AnimeReviewResponse.toReview(): Review = Review(
    reviewId = reviewId,
    userId = userId,
    nickname = nickname,
    profileImageUrl = profileImageUrl,
    content = content,
    createdAt = createdAt,
    rating = rating,
    isSpoiler = isSpoiler,
    likeCount = likeCount,
    isLiked = isLiked,
    isMine = isMine,
)
