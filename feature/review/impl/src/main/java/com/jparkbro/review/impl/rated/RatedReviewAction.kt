package com.jparkbro.review.impl.rated

import com.jparkbro.core.model.review.ReviewSort

sealed interface RatedReviewAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : RatedReviewAction

    data object OnBackClick : Navigation
    data class OnAnimeClick(val animeId: Long) : Navigation
    data class OnEditClick(val animeId: Long) : Navigation
    data class OnRatedSortSelected(val sort: ReviewSort) : RatedReviewAction
    data class OnReviewOnlyToggle(val enabled: Boolean) : RatedReviewAction
    data object OnLoadMore : RatedReviewAction
    data object OnRetryClick : RatedReviewAction
    data class OnDeleteClick(val reviewId: Long) : RatedReviewAction
    data object OnDeleteConfirm : RatedReviewAction
    data object OnDeleteDismiss : RatedReviewAction
}
