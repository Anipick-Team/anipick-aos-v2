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

/**
 * 자신의 아래쪽에 그림자를 그린다. 상단 고정 바(앱바 등)가 콘텐츠 위에 떠 있는 느낌을 줄 때 사용.
 * 피그마 스펙(X 0 / Y 2 / Blur 12 / Spread 0 / #000000 20%) 기준 — [offsetY]가 Y, [height]가 Blur에 대응한다.
 */
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

/**
 * 피그마 스타일(blur/x/y/색상 독립 지정) 드롭섀도우. 카드/스낵바처럼 사각형 배경 뒤에 은은하게 퍼지는
 * 그림자가 필요할 때 사용. [topEdgeShadow]/[bottomEdgeShadow]와 달리 선형 그라데이션이 아니라
 * 실제 블러 처리된 그림자를 그린다.
 *
 * @param blurRadius 그림자가 퍼지는 정도(블러 반경)
 * @param offsetY 그림자의 세로 방향 오프셋
 * @param offsetX 그림자의 가로 방향 오프셋
 * @param color 그림자 색상(투명도 포함) — 기본값은 검정 20%
 * @param cornerRadius 대상의 모서리 반경. 배경의 [RoundedCornerShape]와 값을 맞춰야 자연스럽다.
 */
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