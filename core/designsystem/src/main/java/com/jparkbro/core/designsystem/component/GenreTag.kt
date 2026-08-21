package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 장르 하나를 나타내는 알약 모양 태그 - 애니 카드/리스트 아이템의 장르 목록에서 쓴다. */
@Composable
fun AniPickGenreTag(
    genre: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = genre,
        style = AniPickTheme.typography.caption2,
        color = AniPickTheme.colors.primary,
        modifier = modifier
            .background(AniPickTheme.colors.primary10, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickGenreTagPreview() {
    AniPickGenreTag(genre = "액션")
}
