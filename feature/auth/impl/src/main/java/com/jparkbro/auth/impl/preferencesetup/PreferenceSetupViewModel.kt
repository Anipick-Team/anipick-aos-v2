package com.jparkbro.auth.impl.preferencesetup

import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.data.anime.AnimeRepository
import com.jparkbro.core.data.common.CommonRepository
import com.jparkbro.core.data.review.ReviewRepository
import com.jparkbro.core.model.anime.AnimeRating
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class PreferenceSetupViewModel(
    private val animeRepository: AnimeRepository,
    private val commonRepository: CommonRepository,
    private val reviewRepository: ReviewRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(PreferenceSetupState())
    val state: StateFlow<PreferenceSetupState> = _state.asStateFlow()

    private val _events = Channel<PreferenceSetupEvent>()
    val events = _events.receiveAsFlow()

    init {
        fetchMetadata()
        searchAnimes(resetCursor = true)
    }

    fun onAction(action: PreferenceSetupAction) {
        when (action) {
            PreferenceSetupAction.OnSkipClick -> skip()

            PreferenceSetupAction.OnSearchClick -> searchAnimes(resetCursor = true)

            PreferenceSetupAction.OnSearchClearClick -> {
                _state.value.searchFieldState.clearText()
            }

            PreferenceSetupAction.OnLoadMore -> {
                val current = _state.value
                if (current.isSearchLoading || current.isLoadingMore || current.isLastPage) return
                searchAnimes(resetCursor = false)
            }

            is PreferenceSetupAction.OnFilterChipClick -> {
                _state.update { it.copy(activeFilterSheet = action.filterType) }
            }

            PreferenceSetupAction.OnFilterSheetDismiss -> {
                _state.update { it.copy(activeFilterSheet = null) }
            }

            is PreferenceSetupAction.OnAnimeFilterConfirm -> {
                _state.update {
                    it.copy(
                        selectedYear = action.year,
                        selectedSeason = action.season,
                        selectedGenre = action.genre,
                        activeFilterSheet = null,
                    )
                }
                searchAnimes(resetCursor = true)
            }

            is PreferenceSetupAction.OnSaveRatingClick -> {
                val animeId = _state.value.animeList.getOrNull(action.index)?.animeId ?: return
                _state.update { current ->
                    val withoutExisting = current.ratings.filterNot { it.animeId == animeId }
                    current.copy(
                        ratings = if (action.rating == 0f) {
                            withoutExisting
                        } else {
                            withoutExisting + AnimeRating(animeId = animeId, rating = action.rating)
                        }
                    )
                }
            }

            is PreferenceSetupAction.OnCancelRatingClick -> {
                val animeId = _state.value.animeList.getOrNull(action.index)?.animeId ?: return
                _state.update { current ->
                    current.copy(ratings = current.ratings.filterNot { it.animeId == animeId })
                }
            }

            PreferenceSetupAction.OnCompleteClick -> complete()
        }
    }

    private fun searchAnimes(resetCursor: Boolean) {
        viewModelScope.launch {
            _state.update {
                if (resetCursor) {
                    it.copy(isSearchLoading = true, isSearchError = false)
                } else {
                    it.copy(isLoadingMore = true)
                }
            }

            val current = _state.value
            val query = current.searchFieldState.text.toString().ifBlank { null }
            val lastId = if (resetCursor) null else current.cursor?.lastId

            animeRepository.searchPreferenceSetupAnimes(
                query = query,
                year = current.selectedYear?.toString(),
                season = current.selectedSeason?.id,
                genres = current.selectedGenre?.id,
                lastId = lastId,
            )
                .onSuccess { result ->
                    _state.update { state ->
                        state.copy(
                            animeList = if (resetCursor) result.animes else state.animeList + result.animes,
                            cursor = result.cursor,
                            isSearchLoading = false,
                            isSearchError = false,
                            isLoadingMore = false,
                            isLastPage = result.animes.isEmpty(),
                        )
                    }
                }
                .onFailure { error ->
                    Timber.e("애니메이션 검색 실패: $error")
                    _state.update {
                        it.copy(
                            isSearchLoading = false,
                            isLoadingMore = false,
                            isSearchError = if (resetCursor) true else it.isSearchError,
                        )
                    }
                }
        }
    }

    private fun skip() {
        viewModelScope.launch {
            reviewRepository.submitReviews(emptyList())
                .onFailure { error -> Timber.e("건너뛰기 평가 제출 실패: $error") }
            sendEvent(PreferenceSetupEvent.NavigateToHome)
        }
    }

    private fun complete() {
        viewModelScope.launch {
            _state.update { it.copy(isCompleting = true) }

            reviewRepository.submitReviews(_state.value.ratings)
                .onSuccess {
                    _state.update { it.copy(isCompleting = false) }
                    sendEvent(PreferenceSetupEvent.NavigateToHome)
                }
                .onFailure { error ->
                    Timber.e("평가 제출 실패: $error")
                    _state.update { it.copy(isCompleting = false) }
                }
        }
    }

    private fun fetchMetadata() {
        viewModelScope.launch {
            commonRepository.getMetadata()
                .onSuccess { metadata ->
                    _state.update { it.copy(metadata = metadata) }
                }
                .onFailure { error ->
                    Timber.e("메타데이터 조회 실패: $error")
                }
        }
    }

    private fun sendEvent(event: PreferenceSetupEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}
