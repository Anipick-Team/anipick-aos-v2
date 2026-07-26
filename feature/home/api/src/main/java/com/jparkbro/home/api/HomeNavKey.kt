package com.jparkbro.home.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface HomeNavKey : NavKey {

    @Serializable
    data object Main : HomeNavKey

    @Serializable
    data object Detail : HomeNavKey
}