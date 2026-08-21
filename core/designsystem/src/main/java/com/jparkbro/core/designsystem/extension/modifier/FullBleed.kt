package com.jparkbro.core.designsystem.extension.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp

/** 부모의 좌우 [inset]만큼 더 넓게 측정/배치해 패딩 안에서도 컨테이너 끝까지 꽉 차 보이게 한다. */
fun Modifier.fullBleedHorizontal(inset: Dp): Modifier = layout { measurable, constraints ->
    val insetPx = inset.roundToPx()
    val placeable = measurable.measure(constraints.copy(maxWidth = constraints.maxWidth + insetPx * 2))
    layout(placeable.width, placeable.height) {
        placeable.place(-insetPx, 0)
    }
}
