package com.jparkbro.core.network.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class EmailUpdateRequest(
    val newEmail: String,
    val password: String,
)
