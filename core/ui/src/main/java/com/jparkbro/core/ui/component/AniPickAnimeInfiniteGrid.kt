package com.jparkbro.core.ui.component

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.ui.effect.LoadMoreEffect

/** 카드 너비에 맞춰 열 개수가 자동 조정되는 무한스크롤 애니메 그리드 */
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
    cardBackground: AniPickCardBackground = AniPickCardBackground.GRAY,
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

        LoadMoreEffect(state = state, threshold = threshold, onLoadMore = onLoadMore)

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
                    background = cardBackground,
                    onClick = { onAnimeClick(anime) },
                )
            }

            footer?.invoke(this)
        }
    }
}

/** [AniPickAnimeInfiniteGrid]와 같은 레이아웃으로 로딩 중 자리를 채운다. */
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

/** 가용 너비에 맞는 열 개수/카드 너비/간격 계산 */
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
