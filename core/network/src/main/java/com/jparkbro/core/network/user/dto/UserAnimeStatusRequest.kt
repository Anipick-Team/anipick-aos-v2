package com.jparkbro.core.network.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserAnimeStatusRequest(
    val status: String,
)
