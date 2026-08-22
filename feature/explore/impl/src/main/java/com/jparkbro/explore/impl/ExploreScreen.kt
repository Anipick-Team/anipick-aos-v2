package com.jparkbro.explore.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.core.designsystem.component.AniPickShellTopAppBar
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.community.CommunityBoard
import com.jparkbro.core.model.metadata.FilterType
import com.jparkbro.core.model.metadata.Genre
import com.jparkbro.core.ui.component.AniPickAnimeFilterBottomSheet
import com.jparkbro.core.ui.component.toAnimeFilterTab
import com.jparkbro.core.ui.effect.CollapsibleHeader
import com.jparkbro.core.ui.effect.rememberCollapsibleHeaderState
import com.jparkbro.explore.impl.components.ExploreFilterHeader
import com.jparkbro.explore.impl.components.ExploreTabContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ExploreRoot(
    bottomNavigation: @Composable () -> Unit,
    onNavigateToAnimeDetail: (Long) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCommunity: (CommunityBoard) -> Unit,
    viewModel: ExploreViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ExploreScreen(
        state = state,
        bottomNavigation = bottomNavigation,
        onAction = { action ->
            when (action) {
                ExploreAction.OnSearchClick -> onNavigateToSearch()
                is ExploreAction.OnAnimeClick -> onNavigateToAnimeDetail(action.animeId)
                is ExploreAction.OnCommunityBoardClick -> onNavigateToCommunity(action.board)
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun ExploreScreen(
    state: ExploreState,
    bottomNavigation: @Composable () -> Unit,
    onAction: (ExploreAction) -> Unit,
) {
    val filterHeaderState = rememberCollapsibleHeaderState()
    val gridContentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp)

    Scaffold(
        topBar = {
            AniPickShellTopAppBar(
                onSearchClick = { onAction(ExploreAction.OnSearchClick) }
            )
        },
        bottomBar = bottomNavigation,
        containerColor = AniPickTheme.colors.white
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            CollapsibleHeader(state = filterHeaderState) {
                ExploreFilterHeader(
                    state = state,
                    onAction = onAction,
                    modifier = Modifier.background(AniPickTheme.colors.white)
                )
            }

            ExploreTabContent(
                state = state,
                onAction = onAction,
                nestedScrollConnection = filterHeaderState.nestedScrollConnection,
                gridContentPadding = gridContentPadding,
            )
        }
    }

    state.activeFilterSheet?.let { filterType ->
        AniPickAnimeFilterBottomSheet(
            years = state.metadata.seasonYears ?: emptyList(),
            seasons = state.metadata.seasons ?: emptyList(),
            genres = state.metadata.genres ?: emptyList(),
            types = state.metadata.types ?: emptyList(),
            initialYear = state.year,
            initialSeason = state.season,
            initialGenre = null,
            initialType = state.type,
            initialTab = filterType.toAnimeFilterTab(),
            showYearSeasonTab = true,
            showGenreTab = true,
            showTypeTab = true,
            isMetadataError = state.isMetadataError,
            onMetadataRetryClick = { onAction(ExploreAction.OnMetadataRetryClick) },
            allowMultipleGenres = true,
            initialGenres = state.genres,
            initialGenreMatchType = state.genreOp,
            onConfirm = { year, season, _, type, genres, genreMatchType ->
                onAction(ExploreAction.OnFilterConfirm(year, season, genres, genreMatchType, type))
            },
            onDismissRequest = { onAction(ExploreAction.OnFilterSheetDismiss) },
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ExploreScreenPreview() {
    ExploreScreen(
        state = ExploreState(),
        bottomNavigation = {},
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun ExploreScreenMetadataErrorPreview() {
    ExploreScreen(
        state = ExploreState(isMetadataError = true, activeFilterSheet = FilterType.GENRE),
        bottomNavigation = {},
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun ExploreScreenCommunityTabPreview() {
    ExploreScreen(
        state = ExploreState(
            tab = ExploreTab.COMMUNITY,
            communityBoards = listOf(
                CommunityBoard(
                    hasBoard = true,
                    seriesId = 1L,
                    title = "프리렌: 장송의 여행",
                    coverImageUrl = "",
                    genres = listOf(Genre(id = 1, name = "판타지"), Genre(id = 2, name = "모험")),
                    postCount = 128,
                ),
                CommunityBoard(
                    hasBoard = true,
                    seriesId = 2L,
                    title = "진격의 거인",
                    coverImageUrl = "",
                    genres = listOf(Genre(id = 3, name = "액션"), Genre(id = 4, name = "드라마")),
                    postCount = 342,
                ),
            ),
        ),
        bottomNavigation = {},
        onAction = {},
    )
}
