package com.jparkbro.catalog.impl.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.character.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogCharacterViewModel(
    animeId: Long,
    private val characterRepository: CharacterRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogCharacterState(animeId = animeId))
    val state: StateFlow<CatalogCharacterState> = _state.asStateFlow()

    init {
        loadAnimeCharacters(resetCursor = true)
    }

    fun onAction(action: CatalogCharacterAction) {
        when (action) {
            CatalogCharacterAction.OnLoadMore -> loadMore()
            is CatalogCharacterAction.Navigation -> Unit // Root에서 처리한다.
        }
    }

    private fun loadMore() {
        val current = _state.value
        if (current.isLoading || current.isLoadingMore || current.endReached) return
        loadAnimeCharacters(resetCursor = false)
    }

    private fun loadAnimeCharacters(resetCursor: Boolean) {
        viewModelScope.launch {
            val current = _state.value

            _state.update {
                if (resetCursor) it.copy(isLoading = true, error = null) else it.copy(isLoadingMore = true)
            }

            val lastId = if (resetCursor) null else current.cursor?.lastId
            val lastValue = if (resetCursor) null else current.cursor?.lastValue

            characterRepository.getAnimeCharacters(
                animeId = current.animeId,
                lastId = lastId,
                lastValue = lastValue,
                size = PAGE_SIZE,
            )
                .onSuccess { page ->
                    val items = page.items ?: emptyList()
                    _state.update {
                        it.copy(
                            characters = if (resetCursor) items else it.characters + items,
                            cursor = page.cursor,
                            endReached = items.size < PAGE_SIZE || page.cursor == null,
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
