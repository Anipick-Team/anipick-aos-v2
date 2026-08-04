package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

/**
 * 테두리 있는 칩 모양 텍스트 박스 하나. 선택 여부에 따라 테두리/텍스트 색이 바뀐다.
 */
@Composable
fun AniPickSelectChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val chipColor = if (isSelected) AniPickTheme.colors.primary else AniPickTheme.colors.gray

    Text(
        text = text,
        style = AniPickTheme.typography.caption1,
        color = if (isSelected) AniPickTheme.colors.primary else AniPickTheme.colors.black,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AniPickTheme.colors.white)
            .border(1.dp, chipColor, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickSelectChipPreview() {
    Column {
        AniPickSelectChip(text = "판타지", isSelected = true, onClick = {})
        AniPickSelectChip(text = "판타지", isSelected = false, onClick = {})
    }
}
