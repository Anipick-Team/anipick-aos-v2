package com.jparkbro.core.network.auth

import kotlinx.serialization.Serializable

@Serializable
data class TokenRefreshResponse(
    val accessToken: String,
    val refreshToken: String,
)
