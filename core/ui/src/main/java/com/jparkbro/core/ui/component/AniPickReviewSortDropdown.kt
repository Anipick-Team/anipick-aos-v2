package com.jparkbro.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jparkbro.core.designsystem.component.AniPickSortDropdown
import com.jparkbro.core.model.review.ReviewSort

private val reviewSortOptions = listOf(
    ReviewSort.LATEST to "최신순",
    ReviewSort.MOST_LIKED to "좋아요 순",
    ReviewSort.RATING_DESC to "평가 높은 순",
    ReviewSort.RATING_ASC to "평가 낮은 순",
)

/** 리뷰 목록 정렬 드롭다운 - 마이페이지 "평가한 작품", 애니 상세 "리뷰" 탭 등에서 공통으로 쓴다. */
@Composable
fun AniPickReviewSortDropdown(
    selected: ReviewSort,
    onSortSelected: (ReviewSort) -> Unit,
    modifier: Modifier = Modifier,
) {
    AniPickSortDropdown(
        options = reviewSortOptions,
        selected = selected,
        onSortSelected = onSortSelected,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun AniPickReviewSortDropdownPreview() {
    AniPickReviewSortDropdown(selected = ReviewSort.LATEST, onSortSelected = {})
}
