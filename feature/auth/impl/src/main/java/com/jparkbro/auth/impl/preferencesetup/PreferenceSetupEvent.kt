package com.jparkbro.auth.impl.preferencesetup

sealed interface PreferenceSetupEvent {
    data object NavigateToHome : PreferenceSetupEvent
}
