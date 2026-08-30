package com.jparkbro.catalog.impl.anime

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.catalog.impl.anime.components.AnimeStickyTabHeader
import com.jparkbro.catalog.impl.anime.components.CommunityBoardSkeleton
import com.jparkbro.catalog.impl.anime.components.animeHeroSection
import com.jparkbro.catalog.impl.anime.components.animeInfoTabContent
import com.jparkbro.catalog.impl.anime.components.animeInfoTabSkeleton
import com.jparkbro.catalog.impl.anime.components.animeReviewTabContent
import com.jparkbro.core.designsystem.component.AniPickDialog
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.model.community.CommunityBoard
import com.jparkbro.core.ui.component.AniPickReportDialog
import com.jparkbro.core.ui.effect.LoadMoreEffect
import com.jparkbro.core.ui.effect.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun CatalogAnimeRoot(
    animeId: Long,
    onBackClick: () -> Unit,
    onNavigateToStudio: (Long) -> Unit,
    onNavigateToCommunity: (CommunityBoard) -> Unit,
    onNavigateToReviewWrite: (Long) -> Unit,
    viewModel: CatalogAnimeViewModel = koinViewModel(parameters = { parametersOf(animeId) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CatalogAnimeEvent.NavigateToCommunity -> onNavigateToCommunity(event.board)
        }
    }

    CatalogAnimeScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is CatalogAnimeAction.Navigation -> when (action) {
                    CatalogAnimeAction.OnBackClick -> onBackClick()
                    CatalogAnimeAction.OnShareClick -> {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, state.animeDetail.title)
                        }
                        context.startActivity(Intent.createChooser(intent, null))
                    }
                    is CatalogAnimeAction.OnStudioClick -> onNavigateToStudio(action.studioId)
                    CatalogAnimeAction.OnWriteReviewClick -> onNavigateToReviewWrite(animeId)
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CatalogAnimeScreen(
    state: CatalogAnimeState,
    onAction: (CatalogAnimeAction) -> Unit,
) {
    val listState = rememberLazyListState()
    val isTopBarCollapsed by remember {
        derivedStateOf { listState.firstVisibleItemIndex >= 1 }
    }
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    if (state.selectedTab == CatalogAnimeTab.REVIEW) {
        LoadMoreEffect(state = listState, threshold = 3) {
            onAction(CatalogAnimeAction.OnLoadMoreReviews)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .background(AniPickTheme.colors.white),
        ) {
            animeHeroSection(state = state, onAction = onAction)

            stickyHeader {
                AnimeStickyTabHeader(
                    state = state,
                    onAction = onAction,
                    isTopBarCollapsed = isTopBarCollapsed,
                )
            }

            when (state.selectedTab) {
                CatalogAnimeTab.INFO -> if (state.isLoading) {
                    animeInfoTabSkeleton()
                } else {
                    animeInfoTabContent(
                        detail = state.animeDetail,
                        cast = state.cast,
                        series = state.series,
                        recommendations = state.recommendations,
                        isDescriptionExpanded = isDescriptionExpanded,
                        onToggleDescriptionExpanded = { isDescriptionExpanded = !isDescriptionExpanded },
                        onCastMoreClick = {},
                        onSeriesMoreClick = {},
                        onRecommendationMoreClick = {},
                        onStudioClick = { studioId -> onAction(CatalogAnimeAction.OnStudioClick(studioId)) },
                    )
                }
                CatalogAnimeTab.REVIEW -> animeReviewTabContent(
                    myReview = state.myReview,
                    myReviewDraftRating = state.myReviewDraftRating,
                    reviewCount = state.animeDetail.reviewCount ?: 0,
                    reviews = state.reviews,
                    isReviewsLoading = state.isReviewsLoading,
                    isLoadingMoreReviews = state.isLoadingMoreReviews,
                    reviewSort = state.reviewSort,
                    onReviewSortChanged = { sort -> onAction(CatalogAnimeAction.OnReviewSortChanged(sort)) },
                    showSpoilerReviews = state.showSpoilerReviews,
                    onSpoilerToggle = { enabled -> onAction(CatalogAnimeAction.OnSpoilerToggle(enabled)) },
                    onWriteReviewClick = { onAction(CatalogAnimeAction.OnWriteReviewClick) },
                    onMyReviewRatingChange = { rating -> onAction(CatalogAnimeAction.OnMyReviewRatingChange(rating)) },
                    onMyReviewRatingChangeFinished = { onAction(CatalogAnimeAction.OnMyReviewRatingChangeFinished) },
                    onReviewLikeClick = { reviewId -> onAction(CatalogAnimeAction.OnReviewLikeClick(reviewId)) },
                    onReviewEditClick = { onAction(CatalogAnimeAction.OnWriteReviewClick) },
                    onReviewDeleteClick = { reviewId -> onAction(CatalogAnimeAction.OnReviewDeleteClick(reviewId)) },
                    onReviewReportClick = { reviewId -> onAction(CatalogAnimeAction.OnReviewReportClick(reviewId)) },
                    onReviewBlockClick = { userId -> onAction(CatalogAnimeAction.OnReviewBlockClick(userId)) },
                )
                CatalogAnimeTab.COMMUNITY -> Unit
            }
        }

        if (state.isCommunityBoardLoading) {
            CommunityBoardSkeleton()
        }
    }

    if (state.showCreateCommunityDialog) {
        AniPickDialog(
            title = "알림",
            message = "'${state.animeDetail.title}'의 커뮤니티는 아직 생성되지 않았습니다.\n커뮤니티 생성을 원하실 경우 문의해 주세요.",
            onDismissRequest = { onAction(CatalogAnimeAction.OnCreateCommunityDialogDismiss) },
            confirmText = "문의하기",
            onConfirm = { onAction(CatalogAnimeAction.OnCreateCommunityConfirm) },
            dismissText = "닫기",
            onDismiss = { onAction(CatalogAnimeAction.OnCreateCommunityDialogDismiss) },
        )
    }

    if (state.reportTargetReviewId != null) {
        AniPickReportDialog(
            selectedCategory = state.reportCategory,
            onCategorySelect = { onAction(CatalogAnimeAction.OnReviewReportCategorySelect(it)) },
            onDismissRequest = { onAction(CatalogAnimeAction.OnReviewReportDismiss) },
            onConfirm = { onAction(CatalogAnimeAction.OnReviewReportConfirm) },
            onDismiss = { onAction(CatalogAnimeAction.OnReviewReportDismiss) },
        )
    }

    if (state.deleteTargetReviewId != null) {
        AniPickDialog(
            title = "리뷰 삭제",
            message = "이 리뷰를 삭제하시겠어요?",
            onDismissRequest = { onAction(CatalogAnimeAction.OnReviewDeleteDismiss) },
            confirmText = "삭제",
            onConfirm = { onAction(CatalogAnimeAction.OnReviewDeleteConfirm) },
            dismissText = "취소",
            onDismiss = { onAction(CatalogAnimeAction.OnReviewDeleteDismiss) },
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun CatalogAnimeScreenPreview() {
    CatalogAnimeScreen(
        state = CatalogAnimeState(animeId = 1L),
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun CatalogAnimeScreenSkeletonPreview() {
    CatalogAnimeScreen(
        state = CatalogAnimeState(animeId = 1L, isLoading = true),
        onAction = {},
    )
}

@Composable
@Preview(showBackground = true)
private fun CatalogAnimeScreenCommunityLoadingPreview() {
    CatalogAnimeScreen(
        state = CatalogAnimeState(animeId = 1L, isCommunityBoardLoading = true),
        onAction = {},
    )
}
