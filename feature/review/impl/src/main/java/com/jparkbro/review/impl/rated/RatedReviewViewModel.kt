package com.jparkbro.review.impl.rated

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.review.ReviewRepository
import com.jparkbro.core.data.user.UserRepository
import com.jparkbro.core.ui.GlobalSnackbarManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** 마이페이지 "평가한 작품" 전체보기 - 내가 평가한 애니/리뷰 목록 */
class RatedReviewViewModel(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
) : ViewModel() {

    private val _state = MutableStateFlow(RatedReviewState())
    val state: StateFlow<RatedReviewState> = _state.asStateFlow()

    init {
        load(resetCursor = true)
    }

    fun onAction(action: RatedReviewAction) {
        when (action) {
            is RatedReviewAction.OnRatedSortSelected -> {
                if (_state.value.ratedSort == action.sort) return
                _state.update { it.copy(ratedSort = action.sort, cursor = null, endReached = false) }
                load(resetCursor = true)
            }

            is RatedReviewAction.OnReviewOnlyToggle -> {
                // 이미 불러온 목록 안에서 필터만 하고, 재조회는 하지 않는다.
                _state.update { it.copy(reviewOnly = action.enabled) }
            }

            RatedReviewAction.OnLoadMore -> loadMore()
            RatedReviewAction.OnRetryClick -> load(resetCursor = true)
            is RatedReviewAction.OnDeleteClick -> _state.update { it.copy(deleteTargetReviewId = action.reviewId) }
            RatedReviewAction.OnDeleteConfirm -> onDeleteConfirm()
            RatedReviewAction.OnDeleteDismiss -> dismissDeleteDialog()

            is RatedReviewAction.Navigation -> Unit // Root에서 처리한다.
        }
    }

    private fun onDeleteConfirm() {
        val reviewId = _state.value.deleteTargetReviewId
        dismissDeleteDialog()
        if (reviewId == null) return

        viewModelScope.launch {
            reviewRepository.deleteReview(reviewId)
                .onSuccess {
                    _state.update {
                        it.copy(
                            reviews = it.reviews.filterNot { review -> review.reviewId == reviewId },
                            totalCount = (it.totalCount - 1).coerceAtLeast(0),
                        )
                    }
                    globalSnackbarManager.showSnackbar("리뷰가 삭제되었습니다.")
                }
                .onFailure { error -> globalSnackbarManager.showSnackbar(error.toDisplayMessage()) }
        }
    }

    private fun dismissDeleteDialog() {
        _state.update { it.copy(deleteTargetReviewId = null) }
    }

    private fun loadMore() {
        val current = _state.value
        if (current.isLoading || current.isLoadingMore || current.endReached) return
        if (current.cursor == null) return
        load(resetCursor = false)
    }

    /** "리뷰만 보기"는 재조회 없이 [RatedReviewState.reviewOnly]로 클라이언트에서만 필터링한다 - API에는 넘기지 않는다. */
    private fun load(resetCursor: Boolean) {
        viewModelScope.launch {
            _state.update {
                if (resetCursor) it.copy(isLoading = true, error = null) else it.copy(isLoadingMore = true)
            }

            val current = _state.value
            val lastId = if (resetCursor) null else current.cursor?.lastId

            userRepository.getRatedAnimes(lastId = lastId, size = PAGE_SIZE, sort = current.ratedSort.apiValue)
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            reviews = if (resetCursor) result.items ?: emptyList() else it.reviews + (result.items ?: emptyList()),
                            totalCount = result.count ?: it.totalCount,
                            cursor = result.cursor,
                            endReached = (result.items?.size ?: 0) < PAGE_SIZE,
                            isLoading = false,
                            isLoadingMore = false,
                        )
                    }
                }
                .onFailure { error ->
                    val message = error.toDisplayMessage()
                    _state.update {
                        if (resetCursor) it.copy(isLoading = false, error = message) else it.copy(isLoadingMore = false, error = message)
                    }
                }
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
