package com.jparkbro.core.network.auth

import kotlinx.serialization.Serializable

@Serializable
data class EmailSignupRequest(
    val email: String,
    val password: String,
    val termsAndConditions: Boolean,
)
