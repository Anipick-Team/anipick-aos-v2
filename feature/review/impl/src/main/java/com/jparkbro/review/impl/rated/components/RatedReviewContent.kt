package com.jparkbro.review.impl.rated.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickCountLabel
import com.jparkbro.core.designsystem.component.AniPickEmptyState
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.designsystem.component.AniPickSwitch
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.component.AniPickReviewCard
import com.jparkbro.core.ui.component.AniPickReviewCardSkeleton
import com.jparkbro.core.ui.component.AniPickReviewSortDropdown
import com.jparkbro.core.ui.effect.LoadMoreEffect
import com.jparkbro.review.impl.rated.RatedReviewAction
import com.jparkbro.review.impl.rated.RatedReviewState

private const val SKELETON_ITEM_COUNT = 4

/** 마이페이지 "평가한 작품" 목록 - 정렬/리뷰만보기 헤더 + 리뷰 카드 리스트 */
@Composable
internal fun RatedReviewContent(
    state: RatedReviewState,
    onAction: (RatedReviewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    LoadMoreEffect(state = listState, onLoadMore = { onAction(RatedReviewAction.OnLoadMore) })

    // "리뷰만 보기" 활성화 시 서버 필터(reviewOnly)와 별개로 클라이언트에서도 한 번 더 걸러 안전하게 막는다.
    val visibleReviews = if (state.reviewOnly) state.reviews.filter { !it.content.isNullOrBlank() } else state.reviews

    if (state.isLoading) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = false,
        ) {
            item { RatedReviewHeader(state = state, onAction = onAction) }
            items(SKELETON_ITEM_COUNT) { AniPickReviewCardSkeleton(showProfile = false) }
        }
    } else if (visibleReviews.isEmpty()) {
        Column(modifier = modifier) {
            RatedReviewHeader(
                state = state,
                onAction = onAction,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 16.dp),
            )
            AniPickEmptyState(
                message = state.error ?: "아직 평가한 작품이 없어요.",
                onRetryClick = state.error?.let { { onAction(RatedReviewAction.OnRetryClick) } },
                modifier = Modifier.fillMaxSize(),
            )
        }
    } else {
        LazyColumn(
            state = listState,
            modifier = modifier,
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { RatedReviewHeader(state = state, onAction = onAction) }
            items(visibleReviews, key = { it.reviewId ?: it.hashCode() }) { review ->
                AniPickReviewCard(
                    review = review,
                    showProfile = false,
                    onAnimeClick = { review.animeId?.let { onAction(RatedReviewAction.OnAnimeClick(it)) } },
                    onEditClick = { review.animeId?.let { onAction(RatedReviewAction.OnEditClick(it)) } },
                    onDeleteClick = { review.reviewId?.let { onAction(RatedReviewAction.OnDeleteClick(it)) } },
                )
            }
            if (state.isLoadingMore) {
                item { AniPickLoadMoreIndicator() }
            }
        }
    }
}

@Composable
private fun RatedReviewHeader(
    state: RatedReviewState,
    onAction: (RatedReviewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AniPickCountLabel(count = state.totalCount, unit = "개")
            AniPickReviewSortDropdown(
                selected = state.ratedSort,
                onSortSelected = { onAction(RatedReviewAction.OnRatedSortSelected(it)) },
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "리뷰만 보기",
                style = AniPickTheme.typography.body2,
                color = AniPickTheme.colors.textGray,
            )
            AniPickSwitch(
                checked = state.reviewOnly,
                onCheckedChange = { onAction(RatedReviewAction.OnReviewOnlyToggle(it)) },
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun RatedReviewContentEmptyPreview() {
    RatedReviewContent(
        state = RatedReviewState(),
        onAction = {},
    )
}
