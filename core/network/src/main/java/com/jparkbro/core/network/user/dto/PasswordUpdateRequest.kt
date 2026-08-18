package com.jparkbro.core.network.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class PasswordUpdateRequest(
    val currentPassword: String,
    val newPassword: String,
    val confirmNewPassword: String,
)
