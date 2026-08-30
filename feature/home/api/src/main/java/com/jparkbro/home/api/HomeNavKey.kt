package com.jparkbro.home.api

import androidx.navigation3.runtime.NavKey
import com.jparkbro.core.navigation.Navigator
import kotlinx.serialization.Serializable

sealed interface HomeNavKey : NavKey {

    @Serializable
    data object Main : HomeNavKey

    @Serializable
    data class Detail(val type: HomeDetailType) : HomeNavKey
}

fun Navigator.navigateToHomeMain() {
    navigateAndClearStack(HomeNavKey.Main)
}

fun Navigator.navigateToHomeDetail(type: HomeDetailType) {
    navigate(HomeNavKey.Detail(type))
}