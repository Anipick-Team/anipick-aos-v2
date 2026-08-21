package com.jparkbro.core.network.review.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateReviewRequest(
    val content: String,
    val rating: Float,
    val isSpoiler: Boolean,
)
