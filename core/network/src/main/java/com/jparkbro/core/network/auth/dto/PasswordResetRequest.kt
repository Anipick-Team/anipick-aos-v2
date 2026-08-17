package com.jparkbro.core.network.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class PasswordResetRequest(
    val email: String,
    val newPassword: String,
    val checkNewPassword: String,
)
