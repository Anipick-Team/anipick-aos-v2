package com.jparkbro.search.impl.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.ui.AniPickAnimeCard
import com.jparkbro.core.ui.AniPickAnimeCardSkeleton
import com.jparkbro.core.ui.calculateAnimeGridLayout
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

private const val SEARCH_GRID_SKELETON_ITEM_COUNT = 18
private val SEARCH_GRID_HORIZONTAL_PADDING = 20.dp
private val SEARCH_GRID_VERTICAL_SPACING = 16.dp

@Composable
internal fun SearchAnimeGrid(
    animes: List<Anime>,
    isLoading: Boolean,
    onAnimeClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    isLoadingMore: Boolean = false,
    onLoadMore: (() -> Unit)? = null,
    header: (LazyGridScope.() -> Unit)? = null,
) {
    BoxWithConstraints(modifier = modifier) {
        val layout = remember(maxWidth) {
            calculateAnimeGridLayout(availableWidth = maxWidth - SEARCH_GRID_HORIZONTAL_PADDING * 2)
        }
        val gridState = rememberLazyGridState()

        if (onLoadMore != null) {
            val threshold = layout.columns + 1
            val shouldLoadMore by remember(threshold) {
                derivedStateOf {
                    val layoutInfo = gridState.layoutInfo
                    val totalItemsCount = layoutInfo.totalItemsCount
                    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - threshold
                }
            }

            LaunchedEffect(gridState, threshold) {
                snapshotFlow { shouldLoadMore }
                    .distinctUntilChanged()
                    .filter { it }
                    .collect { onLoadMore() }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(layout.columns),
            state = gridState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = SEARCH_GRID_HORIZONTAL_PADDING,
                end = SEARCH_GRID_HORIZONTAL_PADDING,
                top = SEARCH_GRID_VERTICAL_SPACING,
                bottom = SEARCH_GRID_HORIZONTAL_PADDING,
            ),
            horizontalArrangement = Arrangement.spacedBy(layout.horizontalSpacing),
            verticalArrangement = Arrangement.spacedBy(SEARCH_GRID_VERTICAL_SPACING),
            userScrollEnabled = !isLoading,
        ) {
            header?.invoke(this)

            if (isLoading) {
                items(SEARCH_GRID_SKELETON_ITEM_COUNT) {
                    AniPickAnimeCardSkeleton(cardWidth = layout.cardWidth)
                }
            } else {
                items(animes, key = { it.animeId ?: it.hashCode() }) { anime ->
                    AniPickAnimeCard(
                        anime = anime,
                        cardWidth = layout.cardWidth,
                        onClick = { anime.animeId?.let(onAnimeClick) },
                    )
                }
                if (onLoadMore != null && isLoadingMore) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        AniPickLoadMoreIndicator()
                    }
                }
            }
        }
    }
}
