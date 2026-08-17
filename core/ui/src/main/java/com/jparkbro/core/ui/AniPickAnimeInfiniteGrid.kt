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

/**
 * 검색/랭킹/탐색/홈상세 등에서 공통으로 쓰는 애니 카드 그리드.
 * 열 개수/카드 폭/카드 간격을 고정값으로 받지 않고 실제로 그려질 너비([BoxWithConstraints])를 보고
 * [calculateAnimeGridLayout]으로 계산한다 - 폰은 보통 3열, 태블릿/가로모드처럼 넓어지면 열이
 * 자동으로 늘어난다(최대 [maxColumns]).
 *
 * [state]의 마지막 보이는 아이템 인덱스가 끝에서 [loadMoreThreshold]개 이내로 들어오면
 * [onLoadMore]를 호출한다 - 호출 지점(LaunchedEffect)을 화면마다 반복하지 않도록 여기서 처리한다.
 * [loadMoreThreshold]를 안 주면 `열 개수 + 1`을 쓴다 - 예를 들어 3열이면 마지막 줄(끝에서 3개)이
 * 아니라 그 앞 줄이 보이는 시점(끝에서 4번째, `columns + 1`)에 미리 호출돼야 마지막 줄에 닿기 전에
 * 다음 페이지가 붙는다.
 *
 * [footer]는 [header]와 마찬가지로 그리드 맨 끝에 통째로 얹는 자리 - 다음 페이지 로딩 인디케이터 등에 쓴다.
 */
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

/**
 * [AniPickAnimeInfiniteGrid]와 같은 열 개수/카드 폭 계산을 써서 로딩 중 자리를 채운다 - 실제
 * 데이터가 로딩된 뒤 그리드로 자연스럽게 이어지도록 태블릿/가로모드에서도 같은 열 수로 맞춘다.
 */
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

/** [maxWidth]를 재서 [calculateAnimeGridLayout]으로 계산한 결과를 [content]에 넘겨준다 - 실제
 *  그리드와 스켈레톤 그리드가 같은 폭 계산 로직을 공유하도록 묶은 내부 헬퍼. */
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

internal data class AnimeGridLayout(
    val columns: Int,
    val cardWidth: Dp,
    val horizontalSpacing: Dp,
)

/**
 * [availableWidth](contentPadding을 뺀 실제 그리드 너비)에 맞는 열 개수/카드 폭/카드 간격을 고른다.
 * 열을 [maxColumns]부터 [minColumns]까지 내려가며, 카드 간격을 최대로 줬을 때 카드 폭이
 * [minCardWidth] 이상이면 그 열 개수를 채택한다(카드가 [maxCardWidth]보다 커지면 남는 폭은
 * 카드가 아니라 간격을 늘리는 데 쓴다). 최대 간격으로 안 맞으면 간격을 [minHorizontalSpacing]까지
 * 줄여서 같은 열 개수가 여전히 가능한지 한 번 더 본 뒤, 그래도 안 되면 열을 하나 줄여 재시도한다.
 *
 * [minColumns]까지 내려가도 [minCardWidth]를 못 맞추는 극단적으로 좁은 화면에서는, 화면 밖으로
 * 넘치는 것을 막는 게 우선이라 카드 폭이 [minCardWidth]보다 작아지는 걸 허용한다.
 */
internal fun calculateAnimeGridLayout(
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
