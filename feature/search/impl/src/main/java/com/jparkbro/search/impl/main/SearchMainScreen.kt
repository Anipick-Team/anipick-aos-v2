package com.jparkbro.search.impl.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.core.designsystem.component.AniPickSearchTopAppBar
import com.jparkbro.core.designsystem.component.AniPickSectionDivider
import com.jparkbro.core.designsystem.icon.Close
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.ObserveAsEvents
import com.jparkbro.search.impl.components.SearchAnimeGrid
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
                header = {
                    if (state.recentSearches.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "최근 검색어",
                                    style = AniPickTheme.typography.h3,
                                    color = AniPickTheme.colors.black,
                                )
                                Text(
                                    text = "전체 삭제",
                                    style = AniPickTheme.typography.body2,
                                    color = AniPickTheme.colors.textGray,
                                    modifier = Modifier.clickable(
                                        onClick = { onAction(SearchMainAction.OnRecentSearchClearAll) }
                                    ),
                                )
                            }
                        }
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            LaunchedEffect(state.recentSearches) {
                                recentSearchesState.scrollToItem(0)
                            }
                            LazyRow(
                                state = recentSearchesState,
                                modifier = Modifier.padding(bottom = 24.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(state.recentSearches, key = { it }) { query ->
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable(onClick = { onAction(SearchMainAction.OnRecentSearchClick(query)) })
                                            .border(1.dp, AniPickTheme.colors.gray, RoundedCornerShape(8.dp))
                                            .padding(horizontal = 12.dp, vertical = 6.dp),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(
                                            text = query,
                                            style = AniPickTheme.typography.caption1,
                                            color = AniPickTheme.colors.black,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(AniPickTheme.colors.white),
                                        )
                                        Icon(
                                            imageVector = Close,
                                            contentDescription = "$query 최근 검색어 삭제",
                                            tint = AniPickTheme.colors.textGray,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clickable(onClick = { onAction(SearchMainAction.OnRecentSearchRemove(query)) }),
                                        )
                                    }
                                }
                            }
                        }
                    }
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
