package com.jparkbro.core.network.auth

import kotlinx.serialization.Serializable

@Serializable
data class OAuthLoginResponse(
    val reviewCompletedYn: Boolean,
    val userId: Long,
    val nickname: String,
    val token: Token,
) {
    @Serializable
    data class Token(
        val accessToken: String,
        val refreshToken: String,
    )
}
