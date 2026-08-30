package com.jparkbro.catalog.impl.studio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.studio.StudioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogStudioViewModel(
    studioId: Long,
    private val studioRepository: StudioRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogStudioState(studioId = studioId))
    val state: StateFlow<CatalogStudioState> = _state.asStateFlow()

    init {
        loadStudioAnimes(resetCursor = true)
    }

    fun onAction(action: CatalogStudioAction) {
        when (action) {
            CatalogStudioAction.OnLoadMore -> loadMore()
            is CatalogStudioAction.Navigation -> Unit // Root에서 처리한다.
        }
    }

    private fun loadMore() {
        val current = _state.value
        if (current.isLoading || current.isLoadingMore || current.endReached) return
        loadStudioAnimes(resetCursor = false)
    }

    private fun loadStudioAnimes(resetCursor: Boolean) {
        viewModelScope.launch {
            val current = _state.value

            _state.update {
                if (resetCursor) it.copy(isLoading = true, error = null) else it.copy(isLoadingMore = true)
            }

            val lastId = if (resetCursor) null else current.cursor?.lastId
            val lastValue = if (resetCursor) null else current.cursor?.lastValue

            studioRepository.getStudioAnimes(
                studioId = current.studioId,
                lastId = lastId,
                lastValue = lastValue,
                size = PAGE_SIZE,
            )
                .onSuccess { page ->
                    val animes = page.animes ?: emptyList()
                    _state.update {
                        it.copy(
                            studioName = page.studioName,
                            animes = if (resetCursor) animes else it.animes + animes,
                            cursor = page.cursor,
                            endReached = animes.size < PAGE_SIZE || page.cursor == null,
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
