package com.jparkbro.core.network.community.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePostRequest(
    val title: String,
    val content: String,
    val isSpoiler: Boolean,
    val imageIds: List<Long>,
)
