package com.jparkbro.core.network.auth

import kotlinx.serialization.Serializable

@Serializable
data class OAuthLoginRequest(
    val platform: String = "android",
    val code: String,
)
