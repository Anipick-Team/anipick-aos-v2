package com.jparkbro.core.network.image.dto

import kotlinx.serialization.Serializable

@Serializable
data class CommunityPostImageUploadResponse(
    val imageId: Long,
)
