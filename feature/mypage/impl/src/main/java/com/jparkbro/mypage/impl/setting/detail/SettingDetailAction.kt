package com.jparkbro.mypage.impl.setting.detail

sealed interface SettingDetailAction {
    data object OnBackClick : SettingDetailAction
    data object OnSaveClick : SettingDetailAction
    data object OnPasswordVisibilityToggle : SettingDetailAction
    data object OnWithdrawConfirm : SettingDetailAction
    data object OnWithdrawDialogDismiss : SettingDetailAction
}
