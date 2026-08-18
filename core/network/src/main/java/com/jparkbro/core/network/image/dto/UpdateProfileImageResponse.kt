package com.jparkbro.core.network.image.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileImageResponse(
    val imageId: Long,
)
