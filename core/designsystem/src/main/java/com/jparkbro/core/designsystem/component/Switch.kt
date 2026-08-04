package com.jparkbro.core.designsystem.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

/**
 * Material3 [androidx.compose.material3.Switch]는 트랙/썸 크기가 내부적으로 고정돼 있어서
 * modifier로 32x16 같은 작은 사이즈로 줄일 수 없다(그냥 잘리거나 어긋난다). 그래서 직접 그린 스위치.
 */
@Composable
fun AniPickSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 32.dp,
    height: Dp = 16.dp,
    checkedTrackColor: Color = AniPickTheme.colors.primary,
    uncheckedTrackColor: Color = AniPickTheme.colors.gray,
    thumbColor: Color = AniPickTheme.colors.white,
) {
    val thumbPadding = 2.dp
    val thumbSize = height - thumbPadding * 2
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) width - thumbSize - thumbPadding else thumbPadding,
        label = "AniPickSwitchThumbOffset",
    )

    Box(
        modifier = modifier
            .size(width, height)
            .clip(RoundedCornerShape(height / 2))
            .background(if (checked) checkedTrackColor else uncheckedTrackColor)
            .clickable { onCheckedChange(!checked) },
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = thumbOffset)
                .size(thumbSize)
                .clip(CircleShape)
                .background(thumbColor),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickSwitchPreview() {
    var checked by remember { mutableStateOf(true) }
    Box(modifier = Modifier.padding(16.dp)) {
        AniPickSwitch(checked = checked, onCheckedChange = { checked = it })
    }
}
