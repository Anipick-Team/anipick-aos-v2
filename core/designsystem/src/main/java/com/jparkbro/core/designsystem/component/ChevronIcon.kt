package com.jparkbro.core.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.icon.ChevronDown

/** 펼침/닫힘 상태가 바뀔 때마다 항상 같은 방향(시계 방향)으로 180도씩 더 돌아가는 화살표 아이콘. */
@Composable
fun AniPickAnimatedChevronIcon(
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    contentDescription: String? = null,
) {
    var previousExpanded by remember { mutableStateOf(isExpanded) }
    var rotationTarget by remember { mutableFloatStateOf(if (isExpanded) 180f else 0f) }
    if (isExpanded != previousExpanded) {
        rotationTarget += 180f
        previousExpanded = isExpanded
    }

    val rotation by animateFloatAsState(
        targetValue = rotationTarget,
        animationSpec = tween(durationMillis = 300),
        label = "chevronRotation",
    )

    Icon(
        imageVector = ChevronDown,
        tint = tint,
        contentDescription = contentDescription,
        modifier = modifier.rotate(rotation),
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickAnimatedChevronIconPreview() {
    AniPickAnimatedChevronIcon(
        isExpanded = false,
        modifier = Modifier.size(24.dp),
    )
}
