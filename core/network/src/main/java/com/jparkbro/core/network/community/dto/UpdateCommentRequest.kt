package com.jparkbro.core.network.community.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCommentRequest(
    val content: String,
)
