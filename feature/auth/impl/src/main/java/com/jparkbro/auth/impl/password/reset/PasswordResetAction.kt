package com.jparkbro.auth.impl.password.reset

sealed interface PasswordResetAction {
    data object OnBackClick : PasswordResetAction
    data object OnPasswordVisibilityToggle : PasswordResetAction
    data object OnResetClick : PasswordResetAction
}
