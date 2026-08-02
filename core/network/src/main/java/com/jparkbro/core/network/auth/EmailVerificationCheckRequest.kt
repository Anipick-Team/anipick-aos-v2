package com.jparkbro.core.network.auth

import kotlinx.serialization.Serializable

@Serializable
data class EmailVerificationCheckRequest(
    val email: String,
    val code: String,
)
