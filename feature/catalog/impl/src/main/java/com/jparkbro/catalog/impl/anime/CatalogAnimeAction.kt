package com.jparkbro.catalog.impl.anime

import com.jparkbro.core.model.report.ReportCategory
import com.jparkbro.core.model.review.ReviewSort

sealed interface CatalogAnimeAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : CatalogAnimeAction

    data object OnBackClick : Navigation
    data object OnShareClick : Navigation
    data object OnLikeClick : CatalogAnimeAction
    data object OnBannerImageClick : CatalogAnimeAction
    data object OnCoverImageClick : CatalogAnimeAction
    data class OnStudioClick(val studioId: Long) : Navigation
    data class OnWatchStatusClick(val status: String) : CatalogAnimeAction
    data class OnTabChanged(val tab: CatalogAnimeTab) : CatalogAnimeAction
    data class OnReviewSortChanged(val sort: ReviewSort) : CatalogAnimeAction
    data class OnSpoilerToggle(val enabled: Boolean) : CatalogAnimeAction
    data object OnLoadMoreReviews : CatalogAnimeAction
    data object OnWriteReviewClick : Navigation
    data object OnCreateCommunityDialogDismiss : CatalogAnimeAction
    data object OnCreateCommunityConfirm : CatalogAnimeAction
    data class OnMyReviewRatingChange(val rating: Float) : CatalogAnimeAction
    data object OnMyReviewRatingChangeFinished : CatalogAnimeAction
    data class OnReviewLikeClick(val reviewId: Long) : CatalogAnimeAction
    data class OnReviewReportClick(val reviewId: Long) : CatalogAnimeAction
    data class OnReviewReportCategorySelect(val category: ReportCategory) : CatalogAnimeAction
    data object OnReviewReportConfirm : CatalogAnimeAction
    data object OnReviewReportDismiss : CatalogAnimeAction
    data class OnReviewBlockClick(val userId: Long) : CatalogAnimeAction
    data class OnReviewDeleteClick(val reviewId: Long) : CatalogAnimeAction
    data object OnReviewDeleteConfirm : CatalogAnimeAction
    data object OnReviewDeleteDismiss : CatalogAnimeAction
}
