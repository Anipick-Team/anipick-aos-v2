package com.jparkbro.explore.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface ExploreNavKey : NavKey {

    @Serializable
    data object Explore : ExploreNavKey
}