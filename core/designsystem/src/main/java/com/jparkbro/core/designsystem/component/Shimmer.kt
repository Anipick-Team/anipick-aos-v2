package com.jparkbro.core.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

/**
 * 로딩 중 실제 콘텐츠 모양을 흉내 낸 자리표시자(skeleton)에 씌우는 반짝이는(shimmer) 배경.
 * 대각선 그라데이션이 계속 흘러가는 것처럼 무한 반복 애니메이션된다.
 */
@Composable
fun Modifier.shimmerBackground(shape: Shape = RoundedCornerShape(8.dp)): Modifier {
    val transition = rememberInfiniteTransition(label = "AniPickShimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "AniPickShimmerTranslate",
    )

    val shimmerColors = listOf(
        AniPickTheme.colors.gray.copy(alpha = 0.6f),
        AniPickTheme.colors.gray.copy(alpha = 0.2f),
        AniPickTheme.colors.gray.copy(alpha = 0.6f)
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value),
    )

    return this
        .clip(shape)
        .background(brush)
}

/** 위 [shimmerBackground]만 씌운 빈 박스. 이미지/텍스트 자리를 대신하는 가장 기본적인 조각. */
@Composable
fun AniPickShimmerBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
) {
    Box(modifier = modifier.shimmerBackground(shape))
}

@Preview(showBackground = true)
@Composable
private fun AniPickShimmerBoxPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AniPickShimmerBox(modifier = Modifier.size(width = 132.dp, height = 88.dp))
        AniPickShimmerBox(modifier = Modifier.fillMaxWidth(0.7f).height(18.dp))
        AniPickShimmerBox(modifier = Modifier.fillMaxWidth(0.4f).height(14.dp))
    }
}
