package com.jparkbro.auth.impl.password.reset

sealed interface PasswordResetEvent {
    data object NavigateToEmailLogin : PasswordResetEvent
}
