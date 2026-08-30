package com.jparkbro.review.impl.recent

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.core.designsystem.component.AniPickDialog
import com.jparkbro.core.designsystem.component.AniPickTitleTopAppBar
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.component.AniPickReportDialog
import com.jparkbro.review.impl.recent.components.ReviewListContent
import com.jparkbro.review.impl.recent.components.ReviewListSkeleton
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun RecentReviewRoot(
    onBackClick: () -> Unit,
    onNavigateToAnimeDetail: (Long) -> Unit,
    onNavigateToWrite: (Long) -> Unit,
    viewModel: RecentReviewViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RecentReviewScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is RecentReviewAction.Navigation -> when (action) {
                    RecentReviewAction.OnBackClick -> onBackClick()
                    is RecentReviewAction.OnAnimeClick -> onNavigateToAnimeDetail(action.animeId)
                    is RecentReviewAction.OnEditClick -> onNavigateToWrite(action.animeId)
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
private fun RecentReviewScreen(
    state: RecentReviewState,
    onAction: (RecentReviewAction) -> Unit,
) {
    Scaffold(
        topBar = {
            AniPickTitleTopAppBar(
                title = "최근 리뷰",
                onBackClick = { onAction(RecentReviewAction.OnBackClick) },
            )
        },
        containerColor = AniPickTheme.colors.backgroundGray,
    ) { innerPadding ->
        if (state.isLoading) {
            ReviewListSkeleton(modifier = Modifier.padding(innerPadding))
        } else {
            ReviewListContent(
                reviews = state.reviews,
                isLoadingMore = state.isLoadingMore,
                onLoadMore = { onAction(RecentReviewAction.OnLoadMore) },
                onAnimeClick = { animeId -> onAction(RecentReviewAction.OnAnimeClick(animeId)) },
                onLikeClick = { reviewId -> onAction(RecentReviewAction.OnLikeClick(reviewId)) },
                onEditClick = { animeId -> onAction(RecentReviewAction.OnEditClick(animeId)) },
                onDeleteClick = { reviewId -> onAction(RecentReviewAction.OnDeleteClick(reviewId)) },
                onReportClick = { reviewId -> onAction(RecentReviewAction.OnReportClick(reviewId)) },
                onBlockClick = { userId -> onAction(RecentReviewAction.OnBlockClick(userId)) },
                modifier = Modifier.padding(innerPadding),
            )
        }

        if (state.reportTargetReviewId != null) {
            AniPickReportDialog(
                selectedCategory = state.reportCategory,
                onCategorySelect = { onAction(RecentReviewAction.OnReportCategorySelect(it)) },
                onDismissRequest = { onAction(RecentReviewAction.OnReportDismiss) },
                onConfirm = { onAction(RecentReviewAction.OnReportConfirm) },
                onDismiss = { onAction(RecentReviewAction.OnReportDismiss) },
            )
        }

        if (state.deleteTargetReviewId != null) {
            AniPickDialog(
                title = "리뷰 삭제",
                message = "이 리뷰를 삭제하시겠어요?",
                onDismissRequest = { onAction(RecentReviewAction.OnDeleteDismiss) },
                confirmText = "삭제",
                onConfirm = { onAction(RecentReviewAction.OnDeleteConfirm) },
                dismissText = "취소",
                onDismiss = { onAction(RecentReviewAction.OnDeleteDismiss) },
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun RecentReviewScreenPreview() {
    RecentReviewScreen(
        state = RecentReviewState(),
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun RecentReviewScreenSkeletonPreview() {
    RecentReviewScreen(
        state = RecentReviewState(isLoading = true),
        onAction = {},
    )
}
