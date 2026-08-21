package com.jparkbro.core.model.review

data class Review(
    val reviewId: Long? = null,
    val userId: Long? = null,
    val animeId: Long? = null,
    val animeTitle: String? = null,
    val animeCoverImageUrl: String? = null,
    val content: String? = null,
    val nickname: String? = null,
    val profileImageUrl: String? = null,
    val profileImageByteArray: ByteArray? = null,
    val createdAt: String? = null,
    val rating: Float? = null,
    val likeCount: Int? = null,
    val isLiked: Boolean? = null,
    val isMine: Boolean? = null,
    val isSpoiler: Boolean? = null,
    val isAdult: Boolean? = null,
)
