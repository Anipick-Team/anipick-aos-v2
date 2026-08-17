package com.jparkbro.core.network.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class EmailLoginRequest(
    val email: String,
    val password: String,
)
