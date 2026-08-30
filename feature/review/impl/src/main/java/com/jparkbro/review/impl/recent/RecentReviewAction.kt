package com.jparkbro.review.impl.recent

import com.jparkbro.core.model.report.ReportCategory

sealed interface RecentReviewAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : RecentReviewAction

    data object OnBackClick : Navigation
    data object OnLoadMore : RecentReviewAction
    data class OnReportClick(val reviewId: Long) : RecentReviewAction
    data class OnReportCategorySelect(val category: ReportCategory) : RecentReviewAction
    data object OnReportConfirm : RecentReviewAction
    data object OnReportDismiss : RecentReviewAction
    data class OnAnimeClick(val animeId: Long) : Navigation
    data class OnEditClick(val animeId: Long) : Navigation
    data class OnLikeClick(val reviewId: Long) : RecentReviewAction
    data class OnBlockClick(val userId: Long) : RecentReviewAction
    data class OnDeleteClick(val reviewId: Long) : RecentReviewAction
    data object OnDeleteConfirm : RecentReviewAction
    data object OnDeleteDismiss : RecentReviewAction
}
