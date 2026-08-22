package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.icon.Close
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 텍스트 + 삭제(X) 아이콘이 붙은 칩
 *  [onClick] 없음: 칩 전체가 [onRemove], 있음: 칩 본문은 [onClick] / X 아이콘만 [onRemove] */
@Composable
fun AniPickRemovableChip(
    text: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = CircleShape,
    containerColor: Color = AniPickTheme.colors.primary10,
    contentColor: Color = AniPickTheme.colors.primary,
    borderColor: Color? = null,
    removeContentDescription: String = "$text 삭제",
) {
    Row(
        modifier = modifier
            .clip(shape)
            .then(if (borderColor != null) Modifier.border(1.dp, borderColor, shape) else Modifier)
            .background(containerColor)
            .clickable(onClick = onClick ?: onRemove)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = AniPickTheme.typography.caption1,
            color = contentColor,
        )
        Icon(
            imageVector = Close,
            contentDescription = removeContentDescription,
            tint = contentColor,
            modifier = Modifier
                .size(16.dp)
                .then(if (onClick != null) Modifier.clickable(onClick = onRemove) else Modifier),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickRemovableChipPreview() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AniPickRemovableChip(text = "2024", onRemove = {})
        AniPickRemovableChip(
            text = "액션",
            onRemove = {},
            onClick = {},
            shape = RoundedCornerShape(8.dp),
            containerColor = Color.Transparent,
            contentColor = AniPickTheme.colors.black,
            borderColor = AniPickTheme.colors.gray,
        )
    }
}
