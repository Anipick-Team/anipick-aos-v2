package com.jparkbro.core.network.user.dto

import com.jparkbro.core.model.user.AuthProvider
import com.jparkbro.core.model.user.UserSetting
import kotlinx.serialization.Serializable

@Serializable
data class UserSettingResponse(
    val nickname: String? = null,
    val email: String? = null,
    val provider: String? = null,
)

fun UserSettingResponse.toUserSetting(): UserSetting = UserSetting(
    nickname = nickname,
    email = email,
    provider = provider?.let { runCatching { AuthProvider.valueOf(it) }.getOrNull() },
)
