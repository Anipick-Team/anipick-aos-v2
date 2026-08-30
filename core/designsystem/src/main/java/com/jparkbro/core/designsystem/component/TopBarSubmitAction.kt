package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 글쓰기 화면 상단바 "등록/수정" 액션 - 내용 크기에 맞춰 붙는 알약형 버튼(고정 높이의 [AniPickButton]과 다름). */
@Composable
fun AniPickTopBarSubmitAction(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = AniPickTheme.typography.body2,
        color = if (enabled) AniPickTheme.colors.white else AniPickTheme.colors.textGray,
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (enabled) AniPickTheme.colors.primary else AniPickTheme.colors.gray)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickTopBarSubmitActionPreview() {
    AniPickTopBarSubmitAction(text = "등록", enabled = true, onClick = {})
}

@Preview(showBackground = true)
@Composable
private fun AniPickTopBarSubmitActionDisabledPreview() {
    AniPickTopBarSubmitAction(text = "등록", enabled = false, onClick = {})
}
