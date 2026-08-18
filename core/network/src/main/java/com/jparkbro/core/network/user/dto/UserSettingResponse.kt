package com.jparkbro.core.network.user.dto

import com.jparkbro.core.model.user.AuthProvider
import com.jparkbro.core.model.user.UserSetting
import kotlinx.serialization.Serializable

@Serializable
data class UserSettingResponse(
    val nickname: String,
    val email: String,
    val provider: String,
)

fun UserSettingResponse.toUserSetting(): UserSetting = UserSetting(
    nickname = nickname,
    email = email,
    provider = AuthProvider.valueOf(provider),
)
