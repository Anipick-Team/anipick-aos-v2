package com.jparkbro.auth.impl.password.verification

sealed interface PasswordVerificationAction {
    data object OnBackClick : PasswordVerificationAction
    data object OnRequestVerificationCodeClick : PasswordVerificationAction
    data object OnVerifyClick : PasswordVerificationAction

    // SNS 가입 계정 안내 alert
    data object OnAlertDismiss : PasswordVerificationAction
    data object OnSnsLoginClick : PasswordVerificationAction
}
