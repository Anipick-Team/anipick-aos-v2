package com.jparkbro.review.impl.recent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.review.ReviewRepository
import com.jparkbro.core.data.user.UserRepository
import com.jparkbro.core.model.pagination.Cursor
import com.jparkbro.core.model.review.Review
import com.jparkbro.core.ui.GlobalSnackbarManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** 홈 메인 "최근 리뷰" 더보기로 들어오는 전체 리뷰 피드 */
class RecentReviewViewModel(
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
) : ViewModel() {

    private val _state = MutableStateFlow(RecentReviewState())
    val state: StateFlow<RecentReviewState> = _state.asStateFlow()

    init {
        loadReviews()
    }

    fun onAction(action: RecentReviewAction) {
        when (action) {
            RecentReviewAction.OnLoadMore -> loadMore()
            is RecentReviewAction.OnReportClick -> _state.update { it.copy(reportTargetReviewId = action.reviewId) }
            is RecentReviewAction.OnReportCategorySelect -> _state.update { it.copy(reportCategory = action.category) }
            RecentReviewAction.OnReportConfirm -> onReportConfirm()
            RecentReviewAction.OnReportDismiss -> dismissReportDialog()
            is RecentReviewAction.OnLikeClick -> toggleLike(action.reviewId)
            is RecentReviewAction.OnBlockClick -> blockUser(action.userId)
            is RecentReviewAction.OnDeleteClick -> _state.update { it.copy(deleteTargetReviewId = action.reviewId) }
            RecentReviewAction.OnDeleteConfirm -> onDeleteConfirm()
            RecentReviewAction.OnDeleteDismiss -> dismissDeleteDialog()
            is RecentReviewAction.Navigation -> Unit // Root에서 처리한다.
        }
    }

    private fun onReportConfirm() {
        val reviewId = _state.value.reportTargetReviewId
        val category = _state.value.reportCategory
        dismissReportDialog()
        if (reviewId == null || category == null) return

        viewModelScope.launch {
            reviewRepository.reportReview(reviewId, category)
                .onSuccess { globalSnackbarManager.showSnackbar("신고가 접수되었습니다.") }
                .onFailure { error -> globalSnackbarManager.showSnackbar(error.toDisplayMessage()) }
        }
    }

    private fun dismissReportDialog() {
        _state.update { it.copy(reportTargetReviewId = null, reportCategory = null) }
    }

    /** 좋아요 낙관적 토글 - 실패하면 해당 리뷰만 원상복구하고 스낵바를 띄운다. */
    private fun toggleLike(reviewId: Long) {
        val target = _state.value.reviews.find { it.reviewId == reviewId } ?: return
        val wasLiked = target.isLiked == true
        applyLikeState(reviewId, isLiked = !wasLiked)

        viewModelScope.launch {
            val result = if (wasLiked) reviewRepository.unlikeReview(reviewId) else reviewRepository.likeReview(reviewId)
            result.onFailure { error ->
                applyLikeState(reviewId, isLiked = wasLiked)
                globalSnackbarManager.showSnackbar(error.toDisplayMessage())
            }
        }
    }

    private fun applyLikeState(reviewId: Long, isLiked: Boolean) {
        _state.update { state ->
            state.copy(
                reviews = state.reviews.map { review ->
                    if (review.reviewId == reviewId) {
                        review.copy(
                            isLiked = isLiked,
                            likeCount = (review.likeCount ?: 0) + if (isLiked) 1 else -1,
                        )
                    } else {
                        review
                    }
                },
            )
        }
    }

    private fun blockUser(userId: Long) {
        viewModelScope.launch {
            userRepository.blockUser(userId)
                .onSuccess { globalSnackbarManager.showSnackbar("차단되었습니다.") }
                .onFailure { error -> globalSnackbarManager.showSnackbar(error.toDisplayMessage()) }
        }
    }

    private fun onDeleteConfirm() {
        val reviewId = _state.value.deleteTargetReviewId
        dismissDeleteDialog()
        if (reviewId == null) return

        viewModelScope.launch {
            reviewRepository.deleteReview(reviewId)
                .onSuccess {
                    _state.update { it.copy(reviews = it.reviews.filterNot { review -> review.reviewId == reviewId }) }
                    globalSnackbarManager.showSnackbar("리뷰가 삭제되었습니다.")
                }
                .onFailure { error -> globalSnackbarManager.showSnackbar(error.toDisplayMessage()) }
        }
    }

    private fun dismissDeleteDialog() {
        _state.update { it.copy(deleteTargetReviewId = null) }
    }

    private fun loadReviews() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            reviewRepository.getRecentReviewFeed(size = PAGE_SIZE)
                .onSuccess { result -> applyLoadedPage(result.items ?: emptyList(), result.cursor, append = false) }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toDisplayMessage()) }
                }
        }
    }

    /** 이미 로딩 중이거나 마지막 페이지까지 다 불러왔으면(또는 아직 커서가 없으면) 아무것도 하지 않는다. */
    private fun loadMore() {
        val current = _state.value
        if (current.isLoading || current.isLoadingMore || current.endReached) return
        val cursor = current.cursor ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLoadingMore = true) }

            reviewRepository.getRecentReviewFeed(lastId = cursor.lastId, size = PAGE_SIZE)
                .onSuccess { result -> applyLoadedPage(result.items ?: emptyList(), result.cursor, append = true) }
                .onFailure { error ->
                    _state.update { it.copy(isLoadingMore = false, error = error.toDisplayMessage()) }
                }
        }
    }

    private fun applyLoadedPage(reviews: List<Review>, cursor: Cursor?, append: Boolean) {
        _state.update {
            it.copy(
                reviews = if (append) it.reviews + reviews else reviews,
                cursor = cursor,
                endReached = reviews.size < PAGE_SIZE || cursor == null,
                isLoading = false,
                isLoadingMore = false,
            )
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
