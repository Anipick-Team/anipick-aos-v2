package com.jparkbro.core.network.rating.dto

import kotlinx.serialization.Serializable

@Serializable
data class RatingRequest(
    val rating: Float,
)
