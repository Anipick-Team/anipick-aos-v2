package com.jparkbro.auth.impl.email.login

import androidx.compose.foundation.text.input.TextFieldState

data class EmailLoginState(
    val emailState: TextFieldState = TextFieldState(),
    val passwordState: TextFieldState = TextFieldState(),
    val emailError: String? = null,
    val showPassword: Boolean = false,
    val isLoading: Boolean = false,
    val loginError: String? = null,
    val showAccountDeletedDialog: Boolean = false,
    val isLoginEnabled: Boolean = false,
)
