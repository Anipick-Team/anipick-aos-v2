package com.jparkbro.home.impl.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.core.designsystem.component.AniPickEmptyState
import com.jparkbro.core.designsystem.component.AniPickShellTopAppBar
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.home.api.HomeDetailType
import com.jparkbro.home.impl.main.components.MainScreenSkeleton
import com.jparkbro.home.impl.main.components.homeMainSections
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun MainRoot(
    bottomNavigation: @Composable () -> Unit,
    onNavigateToDetail: (HomeDetailType) -> Unit,
    onNavigateToRecentReview: () -> Unit,
    onNavigateToAnimeDetail: (Long) -> Unit,
    onNavigateToRanking: () -> Unit,
    onNavigateToExplore: (year: Int?, season: Int?) -> Unit,
    onNavigateToSearch: () -> Unit,
    viewModel: HomeMainViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    MainScreen(
        state = state,
        bottomNavigation = bottomNavigation,
        onAction = { action ->
            when (action) {
                HomeMainAction.OnSearchClick -> onNavigateToSearch()
                is HomeMainAction.OnAnimeClick -> onNavigateToAnimeDetail(action.animeId)
                HomeMainAction.OnTrendingMoreClick -> onNavigateToRanking()
                HomeMainAction.OnRecommendationMoreClick -> onNavigateToDetail(HomeDetailType.Recommendation())
                HomeMainAction.OnWeeklyMoreClick -> onNavigateToDetail(HomeDetailType.Weekly)
                HomeMainAction.OnRecentReviewMoreClick -> onNavigateToRecentReview()
                HomeMainAction.OnUpcomingSeasonMoreClick -> {
                    onNavigateToExplore(state.upcomingSeason.seasonYear, state.upcomingSeason.season)
                }
                HomeMainAction.OnRecentAnimeRecommendationMoreClick -> {
                    state.recentAnimeRecommendationAnimeId?.let {
                        onNavigateToDetail(HomeDetailType.Recommendation(basedOnAnimeId = it))
                    }
                }
                HomeMainAction.OnComingSoonMoreClick -> onNavigateToDetail(HomeDetailType.ComingSoon)
                is HomeMainAction.OnDaySelected -> viewModel.onAction(action)
                HomeMainAction.OnRetryClick -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun MainScreen(
    state: HomeMainState,
    bottomNavigation: @Composable () -> Unit,
    onAction: (HomeMainAction) -> Unit,
) {
    Scaffold(
        topBar = {
            AniPickShellTopAppBar(
                onSearchClick = { onAction(HomeMainAction.OnSearchClick) }
            )
        },
        bottomBar = bottomNavigation,
        containerColor = AniPickTheme.colors.backgroundGray
    ) { innerPadding ->
        when {
            state.isLoading -> MainScreenSkeleton(modifier = Modifier.padding(innerPadding))

            state.isConnectionError -> AniPickEmptyState(
                message = "네트워크 연결을 확인해주세요.",
                onRetryClick = { onAction(HomeMainAction.OnRetryClick) },
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            )

            else -> LazyColumn(
                modifier = Modifier
                    .padding(innerPadding),
                contentPadding = PaddingValues(vertical = 40.dp),
                verticalArrangement = Arrangement.spacedBy(100.dp)
            ) {
                homeMainSections(state = state, onAction = onAction)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun MainScreenPreview() {
    MainScreen(
        state = HomeMainState(),
        bottomNavigation = {},
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun MainScreenSkeletonPreview() {
    MainScreen(
        state = HomeMainState(isLoading = true),
        bottomNavigation = {},
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun MainScreenConnectionErrorPreview() {
    MainScreen(
        state = HomeMainState(isConnectionError = true),
        bottomNavigation = {},
        onAction = {},
    )
}
