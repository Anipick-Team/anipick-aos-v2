package com.jparkbro.core.network.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class EmailVerificationSendRequest(
    val email: String,
)
