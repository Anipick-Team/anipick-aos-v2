package com.jparkbro.auth.impl.email.signup

sealed interface EmailSignupEvent {
    data object NavigateToPreferenceSetup : EmailSignupEvent
}
