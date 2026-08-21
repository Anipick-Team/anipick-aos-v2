package com.jparkbro.explore.impl.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jparkbro.catalog.api.CatalogNavKey
import com.jparkbro.community.api.CommunityNavKey
import com.jparkbro.explore.api.ExploreNavKey
import com.jparkbro.explore.impl.ExploreRoot
import com.jparkbro.search.api.SearchNavKey
import kr.agromarket.at.core.navigation.Navigator

/** [ExploreNavKey.Explore]의 contentKey */
const val EXPLORE_CONTENT_KEY = "ExploreNavKey.Explore"

fun EntryProviderScope<NavKey>.exploreEntry(
    navigator: Navigator,
    bottomNavigation: @Composable () -> Unit,
) {
    entry<ExploreNavKey.Explore>(clazzContentKey = { EXPLORE_CONTENT_KEY }) {
        ExploreRoot(
            bottomNavigation = bottomNavigation,
            onNavigateToSearch = { navigator.navigate(SearchNavKey.Main) },
            onNavigateToAnimeDetail = { animeId -> navigator.navigate(CatalogNavKey.Anime(animeId)) },
            onNavigateToCommunity = { board ->
                navigator.navigate(
                    CommunityNavKey.Main(
                        seriesId = board.seriesId,
                        title = board.title,
                        coverImageUrl = board.coverImageUrl,
                        genres = board.genres?.mapNotNull { it.name },
                    )
                )
            },
        )
    }
}