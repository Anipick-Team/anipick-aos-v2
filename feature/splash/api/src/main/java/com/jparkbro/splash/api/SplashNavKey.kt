package com.jparkbro.splash.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface SplashNavKey : NavKey {

    @Serializable
    data object Splash : SplashNavKey
}
