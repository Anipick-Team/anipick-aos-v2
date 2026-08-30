package com.jparkbro.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.theme.AniPickTheme

/** 별점 요약/입력 박스 - 애니 상세 "내 리뷰"(읽기 전용), 리뷰 작성 화면(입력)에서 공통으로 쓴다.
 *  [onRatingChange]가 null이면 읽기 전용, 있으면 별을 눌러 평점을 매길 수 있다. */
@Composable
fun AniPickRatingBox(
    rating: Float?,
    modifier: Modifier = Modifier,
    onRatingChange: ((Float) -> Unit)? = null,
    onRatingChangeFinished: (() -> Unit)? = null,
    contentPadding: Dp = 20.dp,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AniPickTheme.colors.lightGray, RoundedCornerShape(8.dp))
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AniPickStarRatingBar(
            rating = rating ?: 0f,
            onRatingChange = onRatingChange ?: {},
            onRatingChangeFinished = onRatingChangeFinished,
            enabled = onRatingChange != null,
        )
        Text(
            text = "(${rating ?: 0f}/5.0)",
            style = AniPickTheme.typography.h2,
            color = if (rating != null && rating > 0f) AniPickTheme.colors.point else AniPickTheme.colors.textGray,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AniPickRatingBoxReadOnlyPreview() {
    AniPickRatingBox(rating = 4.5f)
}

@Preview(showBackground = true)
@Composable
private fun AniPickRatingBoxEditablePreview() {
    AniPickRatingBox(rating = 0f, onRatingChange = {})
}
