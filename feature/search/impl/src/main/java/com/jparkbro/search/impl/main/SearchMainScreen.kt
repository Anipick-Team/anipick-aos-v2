package com.jparkbro.search.impl.main

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.jparkbro.core.ui.effect.ObserveAsEvents
import com.jparkbro.search.impl.components.SearchAnimeGrid
import com.jparkbro.search.impl.main.components.recentSearchesSection
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun SearchMainRoot(
    onBackClick: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToAnimeDetail: (Long) -> Unit,
    viewModel: SearchMainViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SearchMainEvent.NavigateToDetail -> onNavigateToDetail(event.query)
            is SearchMainEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
        }
    }

    SearchMainScreen(
        state = state,
        onAction = { action ->
            when (action) {
                SearchMainAction.OnBackClick -> onBackClick()
                is SearchMainAction.OnRecentSearchClick -> onNavigateToDetail(action.query)
                is SearchMainAction.OnAnimeClick -> onNavigateToAnimeDetail(action.animeId)
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun SearchMainScreen(
    state: SearchMainState,
    onAction: (SearchMainAction) -> Unit,
) {
    val recentSearchesState = rememberLazyListState()

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
        topBar = {
            AniPickSearchTopAppBar(
                state = state.searchFieldState,
                onSearchClick = { onAction(SearchMainAction.OnSearch) },
                onClearClick = { onAction(SearchMainAction.OnSearchClearClick) },
                onBackClick = { onAction(SearchMainAction.OnBackClick) },
                contentPadding = PaddingValues(20.dp),
            )
        },
        containerColor = AniPickTheme.colors.white,
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            AniPickSectionDivider(modifier = Modifier.fillMaxWidth())
            SearchAnimeGrid(
                animes = state.animes,
                isLoading = state.isLoading,
                onAnimeClick = { animeId -> onAction(SearchMainAction.OnAnimeClick(animeId)) },
                modifier = Modifier.weight(1f),
                emptyMessage = state.error ?: "인기 작품이 없어요.",
                onRetryClick = state.error?.let { { onAction(SearchMainAction.OnRetryClick) } },
                header = {
                    recentSearchesSection(
                        recentSearches = state.recentSearches,
                        recentSearchesState = recentSearchesState,
                        onClearAllClick = { onAction(SearchMainAction.OnRecentSearchClearAll) },
                        onRecentSearchClick = { query -> onAction(SearchMainAction.OnRecentSearchClick(query)) },
                        onRecentSearchRemoveClick = { query -> onAction(SearchMainAction.OnRecentSearchRemove(query)) },
                    )
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = "인기 작품",
                            style = AniPickTheme.typography.h3,
                            color = AniPickTheme.colors.black,
                        )
                    }
                },
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SearchMainScreenPreview() {
    SearchMainScreen(
        state = SearchMainState(recentSearches = listOf("귀멸의 칼날", "진격의 거인")),
        onAction = {},
    )
}
