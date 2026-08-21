package com.jparkbro.core.model.user

data class UserSetting(
    val nickname: String? = null,
    val email: String? = null,
    val provider: AuthProvider? = null,
)
