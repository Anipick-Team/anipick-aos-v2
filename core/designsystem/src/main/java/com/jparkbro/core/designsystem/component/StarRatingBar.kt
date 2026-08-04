package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.icon.StarFilled
import com.jparkbro.core.designsystem.icon.StarOutlined
import com.jparkbro.core.designsystem.theme.AniPickTheme
import kotlin.math.roundToInt

/**
 * 드래그(혹은 탭)한 x좌표를 별 5개 폭에 매핑해 0.5 단위로 스냅한 평점을 [onRatingChange]로 알려준다.
 * 손을 뗄 때까지 기다리지 않고 처음 닿는 순간(down) 바로 값을 반영해서, 탭 한 번으로도 평점을 매길 수 있다.
 */
@Composable
fun AniPickStarRatingBar(
    rating: Float,
    onRatingChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    starCount: Int = 5,
    starSize: Dp = 32.dp,
    spacing: Dp = 4.dp,
    filledColor: Color = AniPickTheme.colors.point,
    emptyColor: Color = AniPickTheme.colors.textGray,
) {
    val density = LocalDensity.current
    val segmentPx = with(density) { (starSize + spacing).toPx() }

    fun ratingAt(x: Float): Float {
        val halfStars = (x / segmentPx) * 2f
        return (halfStars.roundToInt() / 2f).coerceIn(0f, starCount.toFloat())
    }

    Row(
        modifier = modifier
            .semantics { contentDescription = "평점 $rating / $starCount" }
            .then(
                if (enabled) {
                    Modifier.pointerInput(starCount, segmentPx) {
                        awaitEachGesture {
                            val down = awaitFirstDown()
                            onRatingChange(ratingAt(down.position.x))
                            do {
                                val event = awaitPointerEvent()
                                val change = event.changes.first()
                                if (change.positionChanged()) {
                                    onRatingChange(ratingAt(change.position.x))
                                    change.consume()
                                }
                            } while (event.changes.any { it.pressed })
                        }
                    }
                } else {
                    Modifier
                }
            ),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(starCount) { index ->
            val fillFraction = (rating - index).coerceIn(0f, 1f)
            Box(modifier = Modifier.size(starSize)) {
                Icon(
                    imageVector = StarOutlined,
                    tint = emptyColor,
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                )
                if (fillFraction > 0f) {
                    Icon(
                        imageVector = StarFilled,
                        tint = filledColor,
                        contentDescription = null,
                        modifier = Modifier
                            .matchParentSize()
                            .drawWithContent {
                                clipRect(right = size.width * fillFraction) {
                                    this@drawWithContent.drawContent()
                                }
                            },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickStarRatingBarPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AniPickStarRatingBar(rating = 0f, onRatingChange = {})
        AniPickStarRatingBar(rating = 1.5f, onRatingChange = {})
        AniPickStarRatingBar(rating = 3f, onRatingChange = {})
        AniPickStarRatingBar(rating = 4.5f, onRatingChange = {})
        AniPickStarRatingBar(rating = 5f, onRatingChange = {})
    }
}
