package com.jparkbro.review.impl.recent.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.component.AniPickLoadMoreIndicator
import com.jparkbro.core.model.review.Review
import com.jparkbro.core.ui.component.AniPickReviewCard
import com.jparkbro.core.ui.component.AniPickReviewCardSkeleton
import com.jparkbro.core.ui.effect.LoadMoreEffect

@Composable
internal fun ReviewListContent(
    reviews: List<Review>,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onAnimeClick: (Long) -> Unit,
    onLikeClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onReportClick: (Long) -> Unit,
    onBlockClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    LoadMoreEffect(state = listState, onLoadMore = onLoadMore)

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(start = 20.dp, top = 40.dp, end = 20.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(reviews, key = { it.reviewId ?: it.hashCode() }) { review ->
            AniPickReviewCard(
                review = review,
                modifier = Modifier
                    .fillMaxWidth(),
                onAnimeClick = { review.animeId?.let(onAnimeClick) },
                onLikeClick = { review.reviewId?.let(onLikeClick) },
                onEditClick = { review.animeId?.let(onEditClick) },
                onDeleteClick = { review.reviewId?.let(onDeleteClick) },
                onReportClick = { review.reviewId?.let(onReportClick) },
                onBlockClick = { review.userId?.let(onBlockClick) },
            )
        }
        if (isLoadingMore) {
            item { AniPickLoadMoreIndicator() }
        }
    }
}

@Composable
internal fun ReviewListSkeleton(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 20.dp, top = 40.dp, end = 20.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = false,
    ) {
        items(6) {
            AniPickReviewCardSkeleton()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewListSkeletonPreview() {
    ReviewListSkeleton()
}
