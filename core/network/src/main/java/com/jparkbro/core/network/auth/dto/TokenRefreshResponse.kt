package com.jparkbro.core.network.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenRefreshResponse(
    val accessToken: String,
    val refreshToken: String,
)
