package com.jparkbro.core.network.review.dto

import com.jparkbro.core.model.review.Review
import kotlinx.serialization.Serializable

@Serializable
data class RecentReviewResponse(
    val reviewId: Long? = null,
    val userId: Long? = null,
    val animeId: Long? = null,
    val animeTitle: String? = null,
    val reviewContent: String? = null,
    val nickname: String? = null,
    val createdAt: String? = null,
)

fun RecentReviewResponse.toReview(): Review = Review(
    reviewId = reviewId,
    userId = userId,
    animeId = animeId,
    animeTitle = animeTitle,
    content = reviewContent,
    nickname = nickname,
    createdAt = createdAt,
)
