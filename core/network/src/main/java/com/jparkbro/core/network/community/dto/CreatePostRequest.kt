package com.jparkbro.core.network.community.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequest(
    val seriesId: Long,
    val title: String,
    val content: String,
    val isSpoiler: Boolean,
    val imageIds: List<Long>,
)
