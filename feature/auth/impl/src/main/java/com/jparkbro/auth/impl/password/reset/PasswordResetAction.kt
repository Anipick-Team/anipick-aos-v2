package com.jparkbro.auth.impl.password.reset

sealed interface PasswordResetAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : PasswordResetAction

    data object OnBackClick : Navigation
    data object OnPasswordVisibilityToggle : PasswordResetAction
    data object OnResetClick : PasswordResetAction
}
