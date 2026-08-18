package com.jparkbro.explore.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.icon.Close
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
internal fun RemovableFilterChip(
    text: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(AniPickTheme.colors.primary10)
            .clickable(onClick = onRemove)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = AniPickTheme.typography.caption1,
            color = AniPickTheme.colors.primary,
        )
        Icon(
            imageVector = Close,
            contentDescription = "$text 필터 해제",
            tint = AniPickTheme.colors.primary,
            modifier = Modifier
                .size(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RemovableFilterChipPreview() {
    RemovableFilterChip(
        text = "2024",
        onRemove = {},
    )
}
