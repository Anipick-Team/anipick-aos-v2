package com.jparkbro.mypage.impl.setting.main

import com.jparkbro.mypage.api.SettingDetailType

sealed interface SettingMainAction {
    data object OnBackClick : SettingMainAction
    data class OnEditProfileClick(val type: SettingDetailType) : SettingMainAction
    data object OnContactClick : SettingMainAction
    data object OnTermsClick : SettingMainAction
    data object OnPrivacyPolicyClick : SettingMainAction
    data object OnOpenSourceLicenseClick : SettingMainAction
    data object OnNoticeClick : SettingMainAction
    data object OnLogoutClick : SettingMainAction
    data object OnLogoutConfirm : SettingMainAction
    data object OnLogoutDialogDismiss : SettingMainAction
}
