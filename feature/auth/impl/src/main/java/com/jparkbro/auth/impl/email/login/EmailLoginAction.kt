package com.jparkbro.auth.impl.email.login

sealed interface EmailLoginAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : EmailLoginAction

    data object OnPasswordVisibilityToggle : EmailLoginAction
    data object OnLoginClick : EmailLoginAction
    data object OnEmailSignupClick : Navigation
    data object OnFindPasswordClick : Navigation
    data object OnBackClick : Navigation
    data object OnDialogDismiss : EmailLoginAction
}
