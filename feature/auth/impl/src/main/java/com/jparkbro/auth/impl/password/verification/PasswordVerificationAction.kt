package com.jparkbro.auth.impl.password.verification

sealed interface PasswordVerificationAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : PasswordVerificationAction

    data object OnBackClick : Navigation
    data object OnRequestVerificationCodeClick : PasswordVerificationAction
    data object OnVerifyClick : PasswordVerificationAction

    // SNS 가입 계정 안내 alert
    data object OnAlertDismiss : PasswordVerificationAction
    data object OnSnsLoginClick : PasswordVerificationAction
}
