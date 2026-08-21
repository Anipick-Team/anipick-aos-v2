package com.jparkbro.home.impl.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jparkbro.catalog.api.CatalogNavKey
import com.jparkbro.explore.api.ExploreEntryFilter
import com.jparkbro.explore.api.ExploreNavKey
import com.jparkbro.home.api.HomeNavKey
import com.jparkbro.home.api.navigateToHomeDetail
import com.jparkbro.home.impl.detail.DetailRoot
import com.jparkbro.home.impl.main.MainRoot
import com.jparkbro.ranking.api.RankingNavKey
import com.jparkbro.review.api.ReviewNavKey
import com.jparkbro.search.api.SearchNavKey
import kr.agromarket.at.core.navigation.Navigator

/** [HomeNavKey.Main]의 contentKey — 다른 모듈(app의 탭 전환 애니메이션 판단 등)에서도 참조할 수 있게 공개. */
const val HOME_MAIN_CONTENT_KEY = "HomeNavKey.Main"
const val HOME_DETAIL_CONTENT_KEY = "HomeNavKey.Detail"

fun EntryProviderScope<NavKey>.homeEntry(
    navigator: Navigator,
    bottomNavigation: @Composable () -> Unit,
) {
    entry<HomeNavKey.Main>(clazzContentKey = { HOME_MAIN_CONTENT_KEY }) {
        MainRoot(
            bottomNavigation = bottomNavigation,
            onNavigateToDetail = navigator::navigateToHomeDetail,
            onNavigateToRecentReview = { navigator.navigate(ReviewNavKey.Recent) },
            onNavigateToSearch = { navigator.navigate(SearchNavKey.Main) },
            onNavigateToAnimeDetail = { animeId -> navigator.navigate(CatalogNavKey.Anime(animeId)) },
            onNavigateToRanking = { navigator.navigate(RankingNavKey.Ranking) },
            onNavigateToExplore = { year, season ->
                ExploreEntryFilter.set(year, season)
                navigator.navigate(ExploreNavKey.Explore)
            },
        )
    }
    entry<HomeNavKey.Detail>(clazzContentKey = { HOME_DETAIL_CONTENT_KEY }) { key ->
        DetailRoot(
            type = key.type,
            onBackClick = navigator::goBack,
            onNavigateToAnimeDetail = { animeId -> navigator.navigate(CatalogNavKey.Anime(animeId)) },
        )
    }
}