package com.jparkbro.core.network.review.dto

import com.jparkbro.core.model.review.Review
import kotlinx.serialization.Serializable

/** 내가 쓴 리뷰. 아직 안 썼으면 [reviewId] 포함 모든 필드가 null로 내려온다. [content]는 평가만
 *  하고 리뷰 글은 안 남겼으면 null. */
@Serializable
data class MyReviewResponse(
    val reviewId: Long? = null,
    val animeId: Long? = null,
    val rating: Float? = null,
    val content: String? = null,
    val isSpoiler: Boolean? = null,
)

fun MyReviewResponse.toReview(): Review = Review(
    reviewId = reviewId,
    animeId = animeId,
    content = content,
    rating = rating,
    isSpoiler = isSpoiler,
    isMine = true,
)
