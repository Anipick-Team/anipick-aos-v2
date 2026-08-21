package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.jparkbro.core.designsystem.theme.AniPickTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlin.math.abs

/** 스크롤해서 가운데 항목을 고르는 휠 피커 - 스크롤이 멈추면 중앙에 가장 가까운 항목으로 선택값이 스냅된다. */
@Composable
fun <T> AniPickWheelPicker(
    items: List<T>,
    selectedItem: T,
    onSelectedItemChange: (T) -> Unit,
    itemLabel: (T) -> String,
    modifier: Modifier = Modifier,
    itemHeight: Dp = 44.dp,
    visibleItemCount: Int = 5,
    backgroundColor: Color = Color.Transparent,
    resetTrigger: Int = 0,
    enabled: Boolean = true,
    onCenteredItemChange: (T) -> Unit = {},
    itemContent: @Composable (item: T, label: String, fraction: Float) -> Unit = { _, label, fraction ->
        AniPickWheelPickerDefaultItem(label = label, fraction = fraction)
    },
    selectionIndicator: @Composable BoxScope.() -> Unit = {
        AniPickWheelPickerDefaultSelectionIndicator(itemHeight = itemHeight)
    },
) {
    val density = LocalDensity.current
    val initialIndex = items.indexOf(selectedItem).coerceAtLeast(0)
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState)
    val verticalPadding = itemHeight * (visibleItemCount / 2)
    val itemHeightPx = with(density) { itemHeight.toPx() }

    val latestSelectedItem by rememberUpdatedState(selectedItem)
    val latestOnSelectedItemChange by rememberUpdatedState(onSelectedItemChange)

    LaunchedEffect(lazyListState, items) {
        snapshotFlow { lazyListState.isScrollInProgress }
            .filter { isScrolling -> !isScrolling }
            .collect {
                val layoutInfo = lazyListState.layoutInfo
                val viewportCenter =
                    (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                val centeredIndex = layoutInfo.visibleItemsInfo.minByOrNull { info ->
                    abs((info.offset + info.size / 2) - viewportCenter)
                }?.index
                if (centeredIndex != null && centeredIndex in items.indices) {
                    val centeredItem = items[centeredIndex]
                    if (centeredItem != latestSelectedItem) {
                        latestOnSelectedItemChange(centeredItem)
                    }
                }
            }
    }

    LaunchedEffect(lazyListState, items) {
        snapshotFlow {
            val layoutInfo = lazyListState.layoutInfo
            val viewportCenter =
                (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
            layoutInfo.visibleItemsInfo.minByOrNull { info ->
                abs((info.offset + info.size / 2) - viewportCenter)
            }?.index
        }
            .mapNotNull { it }
            .distinctUntilChanged()
            .collect { index -> if (index in items.indices) onCenteredItemChange(items[index]) }
    }

    LaunchedEffect(resetTrigger) {
        val targetIndex = items.indexOf(selectedItem)
        if (targetIndex >= 0) {
            lazyListState.animateScrollToItem(targetIndex)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight * visibleItemCount)
            .background(backgroundColor),
    ) {
        LazyColumn(
            state = lazyListState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = verticalPadding),
            userScrollEnabled = enabled,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            itemsIndexed(items, key = { index, _ -> index }) { index, item ->
                val fraction by remember {
                    derivedStateOf {
                        val layoutInfo = lazyListState.layoutInfo
                        val info = layoutInfo.visibleItemsInfo.firstOrNull { it.key == index }
                        val viewportCenter =
                            (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                        info?.let {
                            val rawFraction = ((it.offset + it.size / 2) - viewportCenter) / itemHeightPx
                            abs(rawFraction)
                        } ?: Float.MAX_VALUE
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    contentAlignment = Alignment.Center,
                ) {
                    itemContent(item, itemLabel(item), fraction)
                }
            }
        }

        selectionIndicator()
    }
}

@Composable
fun AniPickWheelPickerDefaultItem(
    label: String,
    fraction: Float,
    selectedColor: Color = AniPickTheme.colors.black,
) {
    val nearColor = AniPickTheme.colors.gray
    val farColor = AniPickTheme.colors.lightGray
    val centerToNear = fraction.coerceIn(0f, 1f)
    val nearToFar = (fraction - 1f).coerceIn(0f, 1f)
    val color = lerp(lerp(selectedColor, nearColor, centerToNear), farColor, nearToFar)

    Text(
        text = label,
        style = AniPickTheme.typography.body2.copy(
            fontSize = lerp(AniPickTheme.typography.h3.fontSize, AniPickTheme.typography.body2.fontSize, centerToNear),
        ),
        color = color,
        maxLines = 1,
    )
}

@Composable
fun BoxScope.AniPickWheelPickerDefaultSelectionIndicator(
    itemHeight: Dp,
) {
    HorizontalDivider(
        modifier = Modifier
            .align(Alignment.Center)
            .offset(y = -(itemHeight / 2)),
        color = AniPickTheme.colors.backgroundGray,
    )
    HorizontalDivider(
        modifier = Modifier
            .align(Alignment.Center)
            .offset(y = itemHeight / 2),
        color = AniPickTheme.colors.backgroundGray,
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickWheelPickerPreview() {
    AniPickWheelPicker(
        items = (2000..2026).toList().reversed(),
        selectedItem = 2026,
        onSelectedItemChange = {},
        itemLabel = { "${it}년" },
    )
}
