package com.jparkbro.ranking.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.designsystem.component.AniPickShellTopAppBar
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.metadata.FilterType
import com.jparkbro.core.ui.AniPickAnimeFilterBottomSheet
import com.jparkbro.core.ui.LoadMoreEffect
import com.jparkbro.core.ui.toAnimeFilterTab
import com.jparkbro.ranking.impl.components.RankingFilterHeader
import com.jparkbro.ranking.impl.components.RankingItemCard
import com.jparkbro.ranking.impl.components.RankingItemCardSkeleton
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun RankingRoot(
    bottomNavigation: @Composable () -> Unit,
    onNavigateToAnimeDetail: (Long) -> Unit,
    onNavigateToSearch: () -> Unit,
    viewModel: RankingViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RankingScreen(
        state = state,
        bottomNavigation = bottomNavigation,
        onAction = { action ->
            when (action) {
                RankingAction.OnSearchClick -> onNavigateToSearch()
                is RankingAction.OnAnimeClick -> onNavigateToAnimeDetail(action.animeId)
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun RankingScreen(
    state: RankingState,
    bottomNavigation: @Composable () -> Unit,
    onAction: (RankingAction) -> Unit,
) {
    val listState = rememberLazyListState()

    LoadMoreEffect(state = listState, onLoadMore = { onAction(RankingAction.OnLoadMore) })

    LaunchedEffect(state.isLoading) {
        if (state.isLoading) {
            listState.scrollToItem(0)
        }
    }

    Scaffold(
        topBar = {
            AniPickShellTopAppBar(
                onSearchClick = { onAction(RankingAction.OnSearchClick) }
            )
        },
        bottomBar = bottomNavigation,
        containerColor = AniPickTheme.colors.white
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(AniPickTheme.colors.white),
            userScrollEnabled = !state.isLoading,
        ) {
            stickyHeader {
                RankingFilterHeader(state = state, onAction = onAction)
            }
            if (state.isLoading) {
                items(RANKING_SKELETON_ITEM_COUNT) {
                    RankingItemCardSkeleton(contentPadding = PaddingValues(20.dp))
                }
            } else {
                items(state.animes, key = { it.animeId ?: it.hashCode() }) { anime ->
                    RankingItemCard(
                        anime = anime,
                        showRankChange = state.rankingType == RankingType.REAL_TIME,
                        onClick = { animeId -> onAction(RankingAction.OnAnimeClick(animeId)) },
                        contentPadding = PaddingValues(20.dp),
                    )
                }
                if (state.isLoadingMore) {
                    item { AniPickLoadMoreIndicator() }
                }
            }
        }
    }

    state.activeFilterSheet?.let { filterType ->
        AniPickAnimeFilterBottomSheet(
            years = state.metadata.seasonYears ?: emptyList(),
            seasons = state.metadata.seasons ?: emptyList(),
            genres = state.metadata.genres ?: emptyList(),
            types = emptyList(),
            initialYear = state.year,
            initialSeason = state.season,
            initialGenre = state.genre,
            initialType = null,
            initialTab = filterType.toAnimeFilterTab(),
            showYearSeasonTab = filterType != FilterType.GENRE,
            showGenreTab = filterType == FilterType.GENRE,
            showTypeTab = false,
            onConfirm = { year, season, genre, _ ->
                onAction(RankingAction.OnAnimeFilterConfirm(year, season, genre))
            },
            onDismissRequest = { onAction(RankingAction.OnFilterSheetDismiss) },
        )
    }
}

private const val RANKING_SKELETON_ITEM_COUNT = 6

@Composable
@Preview(showBackground = true)
private fun RankingScreenPreview() {
    RankingScreen(
        state = RankingState(),
        bottomNavigation = {},
        onAction = {},
    )
}
