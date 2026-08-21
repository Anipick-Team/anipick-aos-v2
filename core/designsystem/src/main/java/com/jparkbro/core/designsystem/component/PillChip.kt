package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
fun AniPickPillChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = AniPickTheme.typography.caption1,
        color = if (isSelected) AniPickTheme.colors.white else AniPickTheme.colors.black,
        modifier = modifier
            .clip(CircleShape)
            .background(if (isSelected) AniPickTheme.colors.primary else AniPickTheme.colors.lightGray)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 2.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickPillChipPreview() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AniPickPillChip(text = "실시간", isSelected = false, onClick = {})
        AniPickPillChip(text = "2024/4분기", isSelected = true, onClick = {})
    }
}
