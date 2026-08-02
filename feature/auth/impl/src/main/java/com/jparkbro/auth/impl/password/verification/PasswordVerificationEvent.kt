package com.jparkbro.auth.impl.password.verification

sealed interface PasswordVerificationEvent {
    data class NavigateToPasswordReset(val email: String) : PasswordVerificationEvent
}
