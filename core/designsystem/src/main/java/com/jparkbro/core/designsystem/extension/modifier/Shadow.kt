package com.jparkbro.core.designsystem.extension.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
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
    offsetY: Dp = 2.dp,
    color: Color = DefaultShadowColor,
): Modifier = drawBehind {
    val shadowHeightPx = height.toPx()
    val offsetYPx = offsetY.toPx()
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, color),
            startY = size.height + offsetYPx + shadowHeightPx,
            endY = size.height + offsetYPx,
        ),
        topLeft = Offset(0f, size.height + offsetYPx),
        size = Size(size.width, shadowHeightPx),
    )
}

/** 피그마 스타일 드롭섀도우 - [topEdgeShadow]/[bottomEdgeShadow]와 달리 실제 블러 처리된 그림자를 그린다. */
fun Modifier.dropShadow(
    blurRadius: Dp = 12.dp,
    offsetY: Dp = 4.dp,
    offsetX: Dp = 0.dp,
    color: Color = Color.Black.copy(alpha = 0.2f),
    cornerRadius: Dp = 8.dp,
): Modifier = drawBehind {
    val paint = Paint().asFrameworkPaint().apply {
        this.color = android.graphics.Color.TRANSPARENT
        setShadowLayer(
            blurRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            color.toArgb(),
        )
    }
    drawIntoCanvas { canvas ->
        canvas.nativeCanvas.drawRoundRect(
            0f,
            0f,
            size.width,
            size.height,
            cornerRadius.toPx(),
            cornerRadius.toPx(),
            paint,
        )
    }
}

private val DefaultShadowColor = Color.Black.copy(alpha = 0.2f)