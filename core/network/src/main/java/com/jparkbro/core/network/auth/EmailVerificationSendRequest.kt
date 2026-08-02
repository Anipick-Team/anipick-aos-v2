package com.jparkbro.core.network.auth

import kotlinx.serialization.Serializable

@Serializable
data class EmailVerificationSendRequest(
    val email: String,
)
