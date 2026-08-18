package com.jparkbro.core.network.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class NicknameUpdateRequest(
    val nickname: String,
)
