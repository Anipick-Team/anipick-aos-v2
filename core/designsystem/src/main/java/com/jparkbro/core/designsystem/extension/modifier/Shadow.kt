package com.jparkbro.core.designsystem.extension.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/** 자신의 위쪽 바깥에 그림자를 그린다. 하단 고정 바(바텀 내비게이션 등)가 콘텐츠 위에 떠 있는 느낌을 줄 때 사용. */
fun Modifier.topEdgeShadow(
    height: Dp = 15.dp,
    color: Color = DefaultShadowColor,
): Modifier = drawBehind {
    val shadowHeightPx = height.toPx()
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, color),
            startY = -shadowHeightPx,
            endY = 0f,
        ),
        topLeft = Offset(0f, -shadowHeightPx),
        size = Size(size.width, shadowHeightPx),
    )
}

/** 자신의 아래쪽에 그림자를 그린다. 상단 고정 바(앱바 등)가 콘텐츠 위에 떠 있는 느낌을 줄 때 사용. */
fun Modifier.bottomEdgeShadow(
    height: Dp = 12.dp,
    color: Color = DefaultShadowColor,
): Modifier = drawBehind {
    val shadowHeightPx = height.toPx()
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, color),
            startY = shadowHeightPx,
            endY = 0f,
        ),
        topLeft = Offset(0f, shadowHeightPx),
        size = Size(size.width, shadowHeightPx),
    )
}

private val DefaultShadowColor = Color.Black.copy(alpha = 0.2f)