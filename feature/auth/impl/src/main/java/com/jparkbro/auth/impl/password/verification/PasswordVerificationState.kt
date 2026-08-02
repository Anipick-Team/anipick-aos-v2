package com.jparkbro.auth.impl.password.verification

import androidx.compose.foundation.text.input.TextFieldState

data class PasswordVerificationState(
    val emailState: TextFieldState = TextFieldState(),
    val codeState: TextFieldState = TextFieldState(),
    val emailError: String? = null,
    val codeError: String? = null,
    val codeRequestState: VerificationCodeRequestState = VerificationCodeRequestState.Idle,
    val codeExpiresInSeconds: Int? = null,
    val isLoading: Boolean = false,
    val isVerifyEnabled: Boolean = false,
    val showSnsLoginAlert: Boolean = false,
)
