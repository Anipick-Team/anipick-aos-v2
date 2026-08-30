package com.jparkbro.core.network.review.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReportReviewRequest(
    val reportCategory: String,
)
