package com.jparkbro.mypage.impl.setting.detail

sealed interface SettingDetailAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : SettingDetailAction

    data object OnBackClick : Navigation
    data object OnSaveClick : SettingDetailAction
    data object OnPasswordVisibilityToggle : SettingDetailAction
    data object OnWithdrawConfirm : SettingDetailAction
    data object OnWithdrawDialogDismiss : SettingDetailAction
}
