package com.jparkbro.splash.impl

sealed interface SplashEvent {
    data object NavigateToHome : SplashEvent
    data object NavigateToLogin : SplashEvent
}
