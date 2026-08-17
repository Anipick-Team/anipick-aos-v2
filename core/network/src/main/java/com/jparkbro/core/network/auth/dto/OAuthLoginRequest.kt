package com.jparkbro.core.network.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class OAuthLoginRequest(
    val platform: String = "android",
    val code: String,
)
