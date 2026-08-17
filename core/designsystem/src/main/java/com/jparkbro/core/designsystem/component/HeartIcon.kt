package com.jparkbro.core.designsystem.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.icon.HeartFilled
import com.jparkbro.core.designsystem.icon.HeartOutlined
import com.jparkbro.core.designsystem.theme.AniPickTheme

/**
 * 좋아요를 누를 때만 살짝 커졌다 돌아오는 바운스 효과
 */
@Composable
fun AniPickAnimatedHeartIcon(
    isLiked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    likedTint: Color = AniPickTheme.colors.point,
    unlikedTint: Color = LocalContentColor.current,
    contentDescription: String? = null,
) {
    var previousLiked by remember { mutableStateOf(isLiked) }
    val scale = remember { Animatable(1f) }

    LaunchedEffect(isLiked) {
        if (isLiked && !previousLiked) {
            scale.animateTo(1.5f, tween(durationMillis = 150, easing = FastOutSlowInEasing))
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium,
                ),
            )
        } else {
            scale.snapTo(1f)
        }
        previousLiked = isLiked
    }

    Icon(
        imageVector = if (isLiked) HeartFilled else HeartOutlined,
        tint = if (isLiked) likedTint else unlikedTint,
        contentDescription = contentDescription,
        modifier = modifier
            .scale(scale.value)
            .clickable(enabled = enabled, onClick = onClick),
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickAnimatedHeartIconPreview() {
    AniPickAnimatedHeartIcon(
        isLiked = true,
        onClick = {},
        modifier = Modifier.size(24.dp),
    )
}
