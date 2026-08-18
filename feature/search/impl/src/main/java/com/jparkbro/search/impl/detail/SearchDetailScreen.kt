package com.jparkbro.search.impl.detail

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.core.designsystem.component.AniPickSearchTopAppBar
import com.jparkbro.core.designsystem.component.AniPickSectionDivider
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.ObserveAsEvents
import com.jparkbro.search.impl.components.SearchActorList
import com.jparkbro.search.impl.components.SearchAnimeGrid
import com.jparkbro.search.impl.components.SearchResultCountHeader
import com.jparkbro.search.impl.components.SearchStudioList
import com.jparkbro.search.impl.components.SearchTypeTabRow
import com.jparkbro.search.impl.main.SearchMainEvent
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun SearchDetailRoot(
    query: String,
    onBackClick: () -> Unit,
    onNavigateToAnimeDetail: (Long) -> Unit,
    onNavigateToActorDetail: (Long) -> Unit,
    onNavigateToStudioDetail: (Long) -> Unit,
    viewModel: SearchDetailViewModel = koinViewModel(parameters = { parametersOf(query) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SearchDetailEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
        }
    }

    SearchDetailScreen(
        state = state,
        onAction = { action ->
            when (action) {
                SearchDetailAction.OnBackClick -> onBackClick()
                is SearchDetailAction.OnAnimeClick -> onNavigateToAnimeDetail(action.animeId)
                is SearchDetailAction.OnActorClick -> onNavigateToActorDetail(action.personId)
                is SearchDetailAction.OnStudioClick -> onNavigateToStudioDetail(action.studioId)
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun SearchDetailScreen(
    state: SearchDetailState,
    onAction: (SearchDetailAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
        topBar = {
            AniPickSearchTopAppBar(
                state = state.searchFieldState,
                onSearchClick = { onAction(SearchDetailAction.OnSearch) },
                onClearClick = { onAction(SearchDetailAction.OnSearchClearClick) },
                onBackClick = { onAction(SearchDetailAction.OnBackClick) },
                contentPadding = PaddingValues(20.dp),
            )
        },
        containerColor = AniPickTheme.colors.white,
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            SearchTypeTabRow(
                searchType = state.searchType,
                animeCount = state.animeCount,
                actorCount = state.actorCount,
                studioCount = state.studioCount,
                onTabClick = { type -> onAction(SearchDetailAction.OnTabChanged(type)) },
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            AniPickSectionDivider(modifier = Modifier.fillMaxWidth())
            when (state.searchType) {
                SearchType.ANIME -> SearchAnimeGrid(
                    animes = state.animeResult.animes,
                    isLoading = state.isLoading,
                    onAnimeClick = { animeId -> onAction(SearchDetailAction.OnAnimeClick(animeId)) },
                    modifier = Modifier.weight(1f),
                    isLoadingMore = state.isLoadingMore,
                    onLoadMore = { onAction(SearchDetailAction.OnLoadMore) },
                    header = {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            SearchResultCountHeader(count = state.animeCount, unit = "개")
                        }
                    },
                )
                SearchType.ACTOR -> SearchActorList(
                    actors = state.actorResult.actors,
                    isLoading = state.isLoading,
                    totalCount = state.actorCount,
                    onActorClick = { personId -> onAction(SearchDetailAction.OnActorClick(personId)) },
                    modifier = Modifier.weight(1f),
                    isLoadingMore = state.isLoadingMore,
                    onLoadMore = { onAction(SearchDetailAction.OnLoadMore) },
                )
                SearchType.STUDIO -> SearchStudioList(
                    studios = state.studioResult.studios,
                    isLoading = state.isLoading,
                    totalCount = state.studioCount,
                    onStudioClick = { studioId -> onAction(SearchDetailAction.OnStudioClick(studioId)) },
                    modifier = Modifier.weight(1f),
                    isLoadingMore = state.isLoadingMore,
                    onLoadMore = { onAction(SearchDetailAction.OnLoadMore) },
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SearchDetailScreenPreview() {
    SearchDetailScreen(
        state = SearchDetailState(animeCount = 12),
        onAction = {},
    )
}
