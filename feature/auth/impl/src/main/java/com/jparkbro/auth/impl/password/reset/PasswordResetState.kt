package com.jparkbro.auth.impl.password.reset

import androidx.compose.foundation.text.input.TextFieldState

data class PasswordResetState(
    val newPasswordState: TextFieldState = TextFieldState(),
    val checkNewPasswordState: TextFieldState = TextFieldState(),
    val showPassword: Boolean = false,
    val isNewPasswordValid: Boolean = false,
    val isPasswordMatch: Boolean = false,
    val isLoading: Boolean = false,
    val isResetEnabled: Boolean = false,
    val error: String? = null,
)
