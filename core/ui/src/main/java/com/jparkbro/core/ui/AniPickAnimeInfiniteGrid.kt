package com.jparkbro.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jparkbro.core.model.anime.Anime
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun AniPickAnimeInfiniteGrid(
    animes: List<Anime>,
    onAnimeClick: (Anime) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    minCardWidth: Dp = 114.dp,
    maxCardWidth: Dp = 140.dp,
    minColumns: Int = 3,
    maxColumns: Int = 6,
    minHorizontalSpacing: Dp = 4.dp,
    maxHorizontalSpacing: Dp = 16.dp,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
    loadMoreThreshold: Int? = null,
    onLoadMore: () -> Unit = {},
    header: (LazyGridScope.() -> Unit)? = null,
    footer: (LazyGridScope.() -> Unit)? = null,
) {
    BoxWithAnimeGridLayout(
        modifier = modifier,
        minCardWidth = minCardWidth,
        maxCardWidth = maxCardWidth,
        minColumns = minColumns,
        maxColumns = maxColumns,
        minHorizontalSpacing = minHorizontalSpacing,
        maxHorizontalSpacing = maxHorizontalSpacing,
        contentPadding = contentPadding,
    ) { layout ->
        val threshold = loadMoreThreshold ?: (layout.columns + 1)

        val shouldLoadMore by remember(threshold) {
            derivedStateOf {
                val layoutInfo = state.layoutInfo
                val totalItemsCount = layoutInfo.totalItemsCount
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - threshold
            }
        }

        LaunchedEffect(state, threshold) {
            snapshotFlow { shouldLoadMore }
                .distinctUntilChanged()
                .filter { it }
                .collect { onLoadMore() }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(layout.columns),
            state = state,
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(layout.horizontalSpacing),
            verticalArrangement = verticalArrangement,
        ) {
            header?.invoke(this)

            items(animes) { anime ->
                AniPickAnimeCard(
                    anime = anime,
                    cardWidth = layout.cardWidth,
                    onClick = { onAnimeClick(anime) },
                )
            }

            footer?.invoke(this)
        }
    }
}

@Composable
fun AniPickAnimeGridSkeleton(
    modifier: Modifier = Modifier,
    itemCount: Int = 9,
    minCardWidth: Dp = 114.dp,
    maxCardWidth: Dp = 140.dp,
    minColumns: Int = 3,
    maxColumns: Int = 6,
    minHorizontalSpacing: Dp = 4.dp,
    maxHorizontalSpacing: Dp = 16.dp,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
) {
    BoxWithAnimeGridLayout(
        modifier = modifier,
        minCardWidth = minCardWidth,
        maxCardWidth = maxCardWidth,
        minColumns = minColumns,
        maxColumns = maxColumns,
        minHorizontalSpacing = minHorizontalSpacing,
        maxHorizontalSpacing = maxHorizontalSpacing,
        contentPadding = contentPadding,
    ) { layout ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(layout.columns),
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(layout.horizontalSpacing),
            verticalArrangement = verticalArrangement,
            userScrollEnabled = false,
        ) {
            items(itemCount) {
                AniPickAnimeCardSkeleton(cardWidth = layout.cardWidth)
            }
        }
    }
}

@Composable
private fun BoxWithAnimeGridLayout(
    modifier: Modifier,
    minCardWidth: Dp,
    maxCardWidth: Dp,
    minColumns: Int,
    maxColumns: Int,
    minHorizontalSpacing: Dp,
    maxHorizontalSpacing: Dp,
    contentPadding: PaddingValues,
    content: @Composable BoxWithConstraintsScope.(AnimeGridLayout) -> Unit,
) {
    BoxWithConstraints(modifier = modifier) {
        val layoutDirection = LocalLayoutDirection.current
        val horizontalPadding = contentPadding.calculateStartPadding(layoutDirection) +
            contentPadding.calculateEndPadding(layoutDirection)
        val availableWidth = maxWidth - horizontalPadding

        val layout = remember(availableWidth, minCardWidth, maxCardWidth, minColumns, maxColumns, minHorizontalSpacing, maxHorizontalSpacing) {
            calculateAnimeGridLayout(
                availableWidth = availableWidth,
                minCardWidth = minCardWidth,
                maxCardWidth = maxCardWidth,
                minColumns = minColumns,
                maxColumns = maxColumns,
                minHorizontalSpacing = minHorizontalSpacing,
                maxHorizontalSpacing = maxHorizontalSpacing,
            )
        }

        content(layout)
    }
}

data class AnimeGridLayout(
    val columns: Int,
    val cardWidth: Dp,
    val horizontalSpacing: Dp,
)

fun calculateAnimeGridLayout(
    availableWidth: Dp,
    minCardWidth: Dp = 114.dp,
    maxCardWidth: Dp = 140.dp,
    minColumns: Int = 3,
    maxColumns: Int = 6,
    minHorizontalSpacing: Dp = 4.dp,
    maxHorizontalSpacing: Dp = 16.dp,
): AnimeGridLayout {
    require(minColumns >= 1 && maxColumns >= minColumns)

    for (columns in maxColumns downTo minColumns) {
        val spacingSlots = columns - 1

        val cardWidthAtMaxSpacing = (availableWidth - maxHorizontalSpacing * spacingSlots) / columns
        if (cardWidthAtMaxSpacing >= minCardWidth) {
            val cardWidth = cardWidthAtMaxSpacing.coerceAtMost(maxCardWidth)
            val spacing = if (spacingSlots > 0) {
                ((availableWidth - cardWidth * columns) / spacingSlots)
                    .coerceIn(minHorizontalSpacing, maxHorizontalSpacing)
            } else {
                maxHorizontalSpacing
            }
            return AnimeGridLayout(columns, cardWidth, spacing)
        }

        val cardWidthAtMinSpacing = (availableWidth - minHorizontalSpacing * spacingSlots) / columns
        if (cardWidthAtMinSpacing >= minCardWidth) {
            return AnimeGridLayout(columns, cardWidthAtMinSpacing.coerceAtMost(maxCardWidth), minHorizontalSpacing)
        }
    }

    val spacingSlots = minColumns - 1
    val cardWidth = (availableWidth - minHorizontalSpacing * spacingSlots) / minColumns
    return AnimeGridLayout(minColumns, cardWidth, minHorizontalSpacing)
}
