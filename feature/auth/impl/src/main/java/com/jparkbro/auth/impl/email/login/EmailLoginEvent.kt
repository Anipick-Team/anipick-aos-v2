package com.jparkbro.auth.impl.email.login

sealed interface EmailLoginEvent {
    data object NavigateToHome : EmailLoginEvent
    data object NavigateToPreferenceSetup : EmailLoginEvent
}
