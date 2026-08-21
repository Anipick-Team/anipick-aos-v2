package com.jparkbro.ranking.impl.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jparkbro.catalog.api.CatalogNavKey
import com.jparkbro.ranking.api.RankingNavKey
import com.jparkbro.ranking.impl.RankingRoot
import com.jparkbro.search.api.SearchNavKey
import kr.agromarket.at.core.navigation.Navigator

const val RANKING_CONTENT_KEY = "RankingNavKey.Ranking"

fun EntryProviderScope<NavKey>.rankingEntry(
    navigator: Navigator,
    bottomNavigation: @Composable () -> Unit,
) {
    entry<RankingNavKey.Ranking>(clazzContentKey = { RANKING_CONTENT_KEY }) {
        RankingRoot(
            bottomNavigation = bottomNavigation,
            onNavigateToSearch = { navigator.navigate(SearchNavKey.Main) },
            onNavigateToAnimeDetail = { animeId -> navigator.navigate(CatalogNavKey.Anime(animeId)) },
        )
    }
}