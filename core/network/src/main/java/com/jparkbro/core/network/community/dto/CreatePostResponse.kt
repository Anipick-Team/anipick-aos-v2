package com.jparkbro.core.network.community.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostResponse(
    val postId: Long,
)
