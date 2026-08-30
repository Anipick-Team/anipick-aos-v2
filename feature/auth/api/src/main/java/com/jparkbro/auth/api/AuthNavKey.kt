package com.jparkbro.auth.api

import androidx.navigation3.runtime.NavKey
import com.jparkbro.core.navigation.Navigator
import kotlinx.serialization.Serializable

sealed interface AuthNavKey : NavKey {

    @Serializable
    data object Login : AuthNavKey

    object Email {
        @Serializable
        data object Login : AuthNavKey

        @Serializable
        data object Signup : AuthNavKey
    }

    object Password {
        @Serializable
        data object Verification : AuthNavKey

        @Serializable
        data class Reset(val email: String) : AuthNavKey
    }

    @Serializable
    data object PreferenceSetup : AuthNavKey
}

fun Navigator.navigateToLogin() {
    navigateAndClearAllStacks(AuthNavKey.Login)
}

fun Navigator.navigateToPreferenceSetup() {
    navigateAndClearStack(AuthNavKey.PreferenceSetup)
}