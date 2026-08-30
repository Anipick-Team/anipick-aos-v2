package com.jparkbro.catalog.impl.recommendation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.recommendation.RecommendationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogRecommendationViewModel(
    basedOnAnimeId: Long,
    private val recommendationRepository: RecommendationRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogRecommendationState(basedOnAnimeId = basedOnAnimeId))
    val state: StateFlow<CatalogRecommendationState> = _state.asStateFlow()

    init {
        loadRecommendations(resetCursor = true)
    }

    fun onAction(action: CatalogRecommendationAction) {
        when (action) {
            CatalogRecommendationAction.OnLoadMore -> loadMore()
            is CatalogRecommendationAction.Navigation -> Unit // Root에서 처리한다.
        }
    }

    private fun loadMore() {
        val current = _state.value
        if (current.isLoading || current.isLoadingMore || current.endReached) return
        loadRecommendations(resetCursor = false)
    }

    private fun loadRecommendations(resetCursor: Boolean) {
        viewModelScope.launch {
            val current = _state.value

            _state.update {
                if (resetCursor) it.copy(isLoading = true, error = null) else it.copy(isLoadingMore = true)
            }

            val lastId = if (resetCursor) null else current.cursor?.lastId

            recommendationRepository.getAnimeRecommendations(
                animeId = current.basedOnAnimeId,
                lastId = lastId,
                size = PAGE_SIZE,
            )
                .onSuccess { result ->
                    val animes = result.animes ?: emptyList()
                    _state.update {
                        it.copy(
                            referenceAnimeTitle = result.referenceAnimeTitle,
                            animes = if (resetCursor) animes else it.animes + animes,
                            cursor = result.cursor,
                            endReached = animes.size < PAGE_SIZE || result.cursor == null,
                            isLoading = false,
                            isLoadingMore = false,
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(error = error.toDisplayMessage(), isLoading = false, isLoadingMore = false) }
                }
        }
    }

    companion object {
        private const val PAGE_SIZE = 18
    }
}
