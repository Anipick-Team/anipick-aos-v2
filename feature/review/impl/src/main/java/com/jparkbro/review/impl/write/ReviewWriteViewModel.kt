package com.jparkbro.review.impl.write

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.anime.AnimeRepository
import com.jparkbro.core.data.review.ReviewRepository
import com.jparkbro.core.ui.GlobalSnackbarManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReviewWriteViewModel(
    animeId: Long,
    private val animeRepository: AnimeRepository,
    private val reviewRepository: ReviewRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
) : ViewModel() {

    private val _state = MutableStateFlow(ReviewWriteState(animeId = animeId))
    val state: StateFlow<ReviewWriteState> = _state.asStateFlow()

    private val _events = Channel<ReviewWriteEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadAnimeInfo()
        loadCurrentReview()
    }

    fun onAction(action: ReviewWriteAction) {
        when (action) {
            is ReviewWriteAction.OnRatingChanged -> _state.update { it.copy(rating = action.rating) }
            is ReviewWriteAction.OnSpoilerToggle -> _state.update { it.copy(isSpoiler = action.enabled) }
            ReviewWriteAction.OnSubmitClick -> onSubmitClick()
            is ReviewWriteAction.Navigation -> Unit // Root에서 처리한다.
        }
    }

    private fun loadAnimeInfo() {
        viewModelScope.launch {
            animeRepository.getAnimeDetailInfo(_state.value.animeId)
                .onSuccess { detail ->
                    _state.update { it.copy(animeTitle = detail.title, animeCoverImageUrl = detail.coverImageUrl) }
                }
                .onFailure { error -> globalSnackbarManager.showSnackbar(error.toDisplayMessage()) }
        }
    }

    private fun loadCurrentReview() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            reviewRepository.getMyReview(_state.value.animeId)
                .onSuccess { review ->
                    _state.update {
                        it.copy(
                            currentReview = review,
                            rating = review.rating ?: 0f,
                            isSpoiler = review.isSpoiler == true,
                            isLoading = false,
                        )
                    }
                    review.content?.let { content -> _state.value.contentState.setTextAndPlaceCursorAtEnd(content) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toDisplayMessage()) }
                }
        }
    }

    private fun onSubmitClick() {
        val current = _state.value
        if (!current.isSubmitEnabled) return

        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true) }

            reviewRepository.updateReview(
                animeId = current.animeId,
                content = current.contentState.text.toString(),
                rating = current.rating,
                isSpoiler = current.isSpoiler,
            )
                .onSuccess {
                    globalSnackbarManager.showSnackbar(if (current.isEditMode) "리뷰가 수정되었습니다." else "리뷰가 등록되었습니다.")
                    _events.send(ReviewWriteEvent.SubmitSuccess)
                }
                .onFailure { error ->
                    _state.update { it.copy(isSubmitting = false) }
                    globalSnackbarManager.showSnackbar(error.toDisplayMessage())
                }
        }
    }
}
