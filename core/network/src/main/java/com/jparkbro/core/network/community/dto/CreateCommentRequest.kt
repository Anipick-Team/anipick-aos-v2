package com.jparkbro.core.network.community.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateCommentRequest(
    val content: String,
    val parentCommentId: Long? = null,
)
