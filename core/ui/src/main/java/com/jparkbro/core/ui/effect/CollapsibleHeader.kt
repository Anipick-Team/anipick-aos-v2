package com.jparkbro.core.ui.effect

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.Layout
import kotlin.math.roundToInt

/** 스크롤 델타에 1:1로 비례해 접히고 펼쳐지는 헤더 상태. [nestedScrollConnection]을 스크롤 컨테이너에 연결해 사용 */
@Stable
class CollapsibleHeaderState {
    var heightPx by mutableFloatStateOf(0f)
        internal set
    var offsetPx by mutableFloatStateOf(0f)
        private set

    val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val newOffset = (offsetPx + available.y).coerceIn(-heightPx, 0f)
            val consumed = newOffset - offsetPx
            offsetPx = newOffset
            return Offset(0f, consumed)
        }
    }
}

@Composable
fun rememberCollapsibleHeaderState(): CollapsibleHeaderState = remember { CollapsibleHeaderState() }

/** [state]의 오프셋만큼 높이를 줄여 배치하는 헤더 */
@Composable
fun CollapsibleHeader(
    state: CollapsibleHeaderState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        content = { Box { content() } },
        modifier = modifier,
    ) { measurables, constraints ->
        val placeable = measurables.first().measure(constraints.copy(minHeight = 0))
        state.heightPx = placeable.height.toFloat()
        val height = (placeable.height + state.offsetPx).roundToInt().coerceIn(0, placeable.height)
        layout(placeable.width, height) {
            placeable.placeRelative(0, state.offsetPx.roundToInt())
        }
    }
}
