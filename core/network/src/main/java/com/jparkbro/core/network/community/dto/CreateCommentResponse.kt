package com.jparkbro.core.network.community.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateCommentResponse(
    val commentId: Long,
)
