package com.jparkbro.catalog.impl.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.series.SeriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogSeriesViewModel(
    animeId: Long,
    animeTitle: String,
    private val seriesRepository: SeriesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogSeriesState(animeId = animeId, animeTitle = animeTitle))
    val state: StateFlow<CatalogSeriesState> = _state.asStateFlow()

    init {
        loadSeries(resetCursor = true)
    }

    fun onAction(action: CatalogSeriesAction) {
        when (action) {
            CatalogSeriesAction.OnLoadMore -> loadMore()
            is CatalogSeriesAction.Navigation -> Unit // Root에서 처리한다.
        }
    }

    private fun loadMore() {
        val current = _state.value
        if (current.isLoading || current.isLoadingMore || current.endReached) return
        loadSeries(resetCursor = false)
    }

    private fun loadSeries(resetCursor: Boolean) {
        viewModelScope.launch {
            val current = _state.value

            _state.update {
                if (resetCursor) it.copy(isLoading = true, error = null) else it.copy(isLoadingMore = true)
            }

            val lastId = if (resetCursor) null else current.cursor?.lastId

            seriesRepository.getAnimeSeries(
                animeId = current.animeId,
                lastId = lastId,
                size = PAGE_SIZE,
            )
                .onSuccess { result ->
                    val animes = result.animes ?: emptyList()
                    _state.update {
                        it.copy(
                            count = result.count ?: 0,
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
