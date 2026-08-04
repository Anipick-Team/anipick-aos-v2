package com.jparkbro.core.network.review

import kotlinx.serialization.Serializable

@Serializable
data class ReviewItem(
    val animeId: Long,
    val rating: Float,
)
