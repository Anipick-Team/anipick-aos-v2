package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

@Composable
fun AniPickFilterChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isExpanded: Boolean = false,
) {
    val chipColor = if (isSelected) AniPickTheme.colors.primary else AniPickTheme.colors.textGray

    Row(
        modifier = modifier
            .clip(CircleShape)
            .border(1.dp, chipColor, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = AniPickTheme.typography.caption1,
            color = AniPickTheme.colors.black,
        )
        AniPickAnimatedChevronIcon(
            isExpanded = isExpanded,
            tint = chipColor,
            contentDescription = if (isExpanded) "$text 필터 접기" else "$text 필터 펼치기",
            modifier = Modifier.size(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickFilterChipPreview() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AniPickFilterChip(
            text = "년도",
            onClick = {}
        )
        AniPickFilterChip(
            text = "년도",
            onClick = {},
            isSelected = true
        )
        AniPickFilterChip(
            text = "년도",
            onClick = {},
            isExpanded = true
        )
    }
}
