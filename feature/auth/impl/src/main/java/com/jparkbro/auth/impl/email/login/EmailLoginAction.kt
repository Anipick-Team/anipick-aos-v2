package com.jparkbro.auth.impl.email.login

sealed interface EmailLoginAction {
    data object OnPasswordVisibilityToggle : EmailLoginAction
    data object OnLoginClick : EmailLoginAction
    data object OnEmailSignupClick : EmailLoginAction
    data object OnFindPasswordClick : EmailLoginAction
}
