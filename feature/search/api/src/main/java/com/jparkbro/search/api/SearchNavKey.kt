package com.jparkbro.search.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kr.agromarket.at.core.navigation.Navigator

sealed interface SearchNavKey : NavKey {

    @Serializable
    data object Main : SearchNavKey

    @Serializable
    data class Detail(val query: String) : SearchNavKey
}