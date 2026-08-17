package com.jparkbro.core.network.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class EmailVerificationCheckRequest(
    val email: String,
    val code: String,
)
