package com.jparkbro.ranking.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface RankingNavKey : NavKey {

    @Serializable
    data object Ranking : RankingNavKey

}