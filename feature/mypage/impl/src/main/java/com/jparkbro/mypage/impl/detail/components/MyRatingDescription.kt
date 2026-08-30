package com.jparkbro.mypage.impl.detail.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.icon.StarFilled
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 마이페이지 "다 본 애니" 카드 설명 자리 - 내 평점이 없으면 안내 문구, 있으면 별 아이콘 + 평점. */
@Composable
internal fun MyRatingDescription(rating: Float?, modifier: Modifier = Modifier) {
    if (rating == null) {
        Text(
            text = "아직 평가가 없어요",
            style = AniPickTheme.typography.caption2,
            color = AniPickTheme.colors.textGray,
            modifier = modifier,
        )
    } else {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "내 평가 ",
                style = AniPickTheme.typography.caption2,
                color = AniPickTheme.colors.textGray,
            )
            Icon(
                imageVector = StarFilled,
                contentDescription = null,
                tint = AniPickTheme.colors.point,
                modifier = Modifier.size(12.dp),
            )
            Text(
                text = " $rating",
                style = AniPickTheme.typography.caption2,
                color = AniPickTheme.colors.point,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyRatingDescriptionNonePreview() {
    MyRatingDescription(rating = null)
}

@Preview(showBackground = true)
@Composable
private fun MyRatingDescriptionRatedPreview() {
    MyRatingDescription(rating = 4.5f)
}
