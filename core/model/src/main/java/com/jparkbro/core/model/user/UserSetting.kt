package com.jparkbro.core.model.user

data class UserSetting(
    val nickname: String,
    val email: String,
    val provider: AuthProvider,
)
