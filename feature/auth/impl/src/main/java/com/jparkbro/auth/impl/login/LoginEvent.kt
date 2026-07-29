package com.jparkbro.auth.impl.login

sealed interface LoginEvent {
    data object NavigateToPreferenceSetup : LoginEvent
    data object NavigateToHome : LoginEvent
}
