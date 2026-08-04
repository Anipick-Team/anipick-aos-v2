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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.jparkbro.core.designsystem.theme.AniPickTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlin.math.abs

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

    // 주의: items의 원소 자체가 null일 수 있다("전체년도"/"전체분기" 같은 항목). 그래서 "센터에 온
    // 아이템 값이 null인지"가 아니라 "센터에 대응하는 index를 찾았는지"로 판단해야 한다 — 아이템 값의
    // null과 "못 찾음"을 구분하지 않으면 null 항목을 선택했을 때 아무 일도 안 일어나는 버그가 생긴다.
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
                    if (centeredItem != selectedItem) {
                        onSelectedItemChange(centeredItem)
                    }
                }
            }
    }

    // 스크롤이 멈추기 전(드래그 중)에도 뷰포트 중앙에 온 아이템을 실시간으로 알려준다.
    // onSelectedItemChange는 스크롤이 완전히 멈춘 뒤에만 값이 확정되므로, "지금 화면에서 가운데에
    // 뭐가 보이는지"에 바로 반응해야 하는 UI(예: 다른 휠의 enabled 상태)는 이걸 써야 한다.
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

/**
 * [AniPickWheelPicker]의 기본 아이템 디자인. 선택된 항목의 색만 호출부에서 바꿔 끼울 수 있고
 * ([selectedColor]), 그 외는 전부 고정이다: [fraction]은 "몇 칸 떨어져 있는지"를 나타내는 값이라
 * (0 = 정중앙, 1 = 바로 옆 칸, 2 이상 = 그보다 먼 칸) — 0~1 구간에서는 [selectedColor]에서
 * [AniPickTheme.colors.gray]로, 1~2 구간에서는 그 gray에서 더 옅은 [AniPickTheme.colors.lightGray]로
 * 부드럽게(lerp) 전환된다. 글자 크기도 같은 0~1 구간에서 h3 -> body2로 줄어들고, 그 뒤로는 body2로 고정된다.
 * 전부 스크롤 중 실시간으로 갱신되는 [fraction] 기준이라 스와이프하는 동안 바로바로 따라온다.
 */
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

/** [AniPickWheelPicker]의 기본 선택 영역 표시(가운데 칸 위/아래 구분선). */
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
