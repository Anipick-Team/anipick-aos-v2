package com.jparkbro.core.network.review.dto

import com.jparkbro.core.model.review.Review
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

/** HomeDetail "더보기" 화면에서 20개씩 무한스크롤로 불러오는 페이지 단위 응답. */
@Serializable
data class RecentReviewFeedResponse(
    val count: Int,
    val cursor: CursorResponse,
    val reviews: List<RecentReviewFeedItemResponse> = emptyList(),
)

@Serializable
data class RecentReviewFeedItemResponse(
    val reviewId: Long? = null,
    val userId: Long? = null,
    val animeId: Long? = null,
    val animeTitle: String? = null,
    val animeCoverImageUrl: String? = null,
    val rating: Float? = null,
    val reviewContent: String? = null,
    val nickname: String? = null,
    val profileImageUrl: String? = null,
    val createdAt: String? = null,
    val likeCount: Int? = null,
    val likedByCurrentUser: Boolean = false,
    val isMine: Boolean = false,
)

fun RecentReviewFeedItemResponse.toReview(): Review = Review(
    reviewId = reviewId,
    userId = userId,
    animeId = animeId,
    animeTitle = animeTitle,
    animeCoverImageUrl = animeCoverImageUrl,
    content = reviewContent,
    nickname = nickname,
    profileImageUrl = profileImageUrl,
    createdAt = createdAt,
    rating = rating,
    likeCount = likeCount,
    isLiked = likedByCurrentUser,
    isMine = isMine,
)
