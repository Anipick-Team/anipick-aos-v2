package com.jparkbro.core.network.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class OAuthLoginResponse(
    val userId: Long,
    val token: Token,
    val reviewCompletedYn: Boolean? = null,
    val nickname: String? = null,
) {
    @Serializable
    data class Token(
        val accessToken: String,
        val refreshToken: String,
    )
}
