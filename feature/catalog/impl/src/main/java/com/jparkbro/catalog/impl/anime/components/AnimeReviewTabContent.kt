package com.jparkbro.catalog.impl.anime.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickButton
import com.jparkbro.core.designsystem.component.AniPickEmptyState
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.designsystem.component.AniPickRatingBox
import com.jparkbro.core.designsystem.component.AniPickSectionDivider
import com.jparkbro.core.designsystem.component.AniPickSwitch
import com.jparkbro.core.designsystem.extension.modifier.fullBleedHorizontal
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.review.Review
import com.jparkbro.core.model.review.ReviewSort
import com.jparkbro.core.ui.component.AniPickReviewCard
import com.jparkbro.core.ui.component.AniPickReviewSortDropdown

/** "리뷰" 탭 콘텐츠 - 필터(스포일러 포함 여부)/정렬 헤더 + 목록. */
internal fun LazyListScope.animeReviewTabContent(
    myReview: Review,
    myReviewDraftRating: Float?,
    reviewCount: Int,
    reviews: List<Review>,
    isReviewsLoading: Boolean,
    isLoadingMoreReviews: Boolean,
    reviewSort: ReviewSort,
    onReviewSortChanged: (ReviewSort) -> Unit,
    showSpoilerReviews: Boolean,
    onSpoilerToggle: (Boolean) -> Unit,
    onWriteReviewClick: () -> Unit,
    onMyReviewRatingChange: (Float) -> Unit,
    onMyReviewRatingChangeFinished: () -> Unit,
    onReviewLikeClick: (Long) -> Unit,
    onReviewEditClick: () -> Unit,
    onReviewDeleteClick: (Long) -> Unit,
    onReviewReportClick: (Long) -> Unit,
    onReviewBlockClick: (Long) -> Unit,
) {
    item {
        MyReviewSummary(
            review = myReview,
            draftRating = myReviewDraftRating,
            onWriteReviewClick = onWriteReviewClick,
            onRatingChange = onMyReviewRatingChange,
            onRatingChangeFinished = onMyReviewRatingChangeFinished,
        )
    }

    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AniPickTheme.colors.white)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = "리뷰",
                    style = AniPickTheme.typography.h1,
                    color = AniPickTheme.colors.black,
                )
                Text(
                    text = "${reviewCount}개",
                    style = AniPickTheme.typography.caption1,
                    color = AniPickTheme.colors.textGray,
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "스포일러",
                    style = AniPickTheme.typography.body2,
                    color = AniPickTheme.colors.primary,
                )
                AniPickSwitch(
                    checked = showSpoilerReviews,
                    onCheckedChange = onSpoilerToggle,
                )
            }
        }
    }

    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AniPickTheme.colors.backgroundGray)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            AniPickReviewSortDropdown(
                selected = reviewSort,
                onSortSelected = onReviewSortChanged,
            )
        }
    }

    when {
        isReviewsLoading -> item {
            AniPickLoadMoreIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AniPickTheme.colors.backgroundGray),
            )
        }
        reviews.isEmpty() -> item {
            AniPickEmptyState(
                message = "아직 작성된 리뷰가 없습니다.",
                modifier = Modifier
                    .fillParentMaxHeight(0.6f)
                    .background(AniPickTheme.colors.backgroundGray),
            )
        }
        else -> {
            itemsIndexed(reviews, key = { index, item -> "$index-${item.reviewId}" }) { _, review ->
                AniPickReviewCard(
                    review = review,
                    showAnimeHeader = false,
                    onLikeClick = { review.reviewId?.let(onReviewLikeClick) },
                    onEditClick = onReviewEditClick,
                    onDeleteClick = { review.reviewId?.let(onReviewDeleteClick) },
                    onReportClick = { review.reviewId?.let(onReviewReportClick) },
                    onBlockClick = { review.userId?.let(onReviewBlockClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AniPickTheme.colors.backgroundGray)
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                )
            }
            if (isLoadingMoreReviews) {
                item {
                    AniPickLoadMoreIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AniPickTheme.colors.backgroundGray),
                    )
                }
            }
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(AniPickTheme.colors.backgroundGray),
                )
            }
        }
    }
}

@Composable
private fun MyReviewSummary(
    review: Review,
    draftRating: Float?,
    onWriteReviewClick: () -> Unit,
    onRatingChange: (Float) -> Unit,
    onRatingChangeFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AniPickRatingBox(
            rating = draftRating ?: review.rating,
            onRatingChange = onRatingChange,
            onRatingChangeFinished = onRatingChangeFinished,
        )
        AniPickButton(
            text = "상세 리뷰 작성하기",
            onClick = onWriteReviewClick,
            modifier = Modifier.fillMaxWidth(),
            // API 성공 후 확정된 review.rating 기준 - 드래그 중인 draftRating은 보지 않는다.
            enabled = (review.rating ?: 0f) > 0f
        )
        AniPickSectionDivider(
            modifier = Modifier.fullBleedHorizontal(20.dp),
        )
    }
}
