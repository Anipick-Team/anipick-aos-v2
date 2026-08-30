package com.jparkbro.search.api

import androidx.navigation3.runtime.NavKey
import com.jparkbro.core.navigation.Navigator
import kotlinx.serialization.Serializable

sealed interface SearchNavKey : NavKey {

    @Serializable
    data object Main : SearchNavKey

    @Serializable
    data class Detail(val query: String) : SearchNavKey
}