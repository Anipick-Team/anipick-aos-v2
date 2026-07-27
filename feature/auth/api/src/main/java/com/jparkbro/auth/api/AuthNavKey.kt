package com.jparkbro.auth.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kr.agromarket.at.core.navigation.Navigator

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
        data class Verification(val email: String) : AuthNavKey

        @Serializable
        data class Reset(val token: String) : AuthNavKey
    }

    @Serializable
    data object PreferenceSetup : AuthNavKey
}

fun Navigator.navigateToLogin() {
    navigateAndClearStack(AuthNavKey.Login)
}