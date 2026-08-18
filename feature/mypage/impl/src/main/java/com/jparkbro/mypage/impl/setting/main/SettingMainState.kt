package com.jparkbro.mypage.impl.setting.main

import com.jparkbro.core.model.user.AuthProvider

data class SettingMainState(
    val nickname: String? = null,
    val email: String? = null,
    val provider: AuthProvider? = null,
    val isLoading: Boolean = false,
    val showLogoutDialog: Boolean = false,
) {
    val canEditCredentials: Boolean
        get() = provider == AuthProvider.LOCAL
}