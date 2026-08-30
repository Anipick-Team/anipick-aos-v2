package com.jparkbro.search.impl.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.search.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchDetailViewModel(
    query: String,
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SearchDetailState(searchFieldState = TextFieldState(query)))
    val state: StateFlow<SearchDetailState> = _state.asStateFlow()

    init {
        search(resetCursor = true)
        saveRecentSearch(query)
    }

    fun onAction(action: SearchDetailAction) {
        when (action) {
            SearchDetailAction.OnSearch -> onSearch()
            SearchDetailAction.OnSearchClearClick -> _state.value.searchFieldState.clearText()
            is SearchDetailAction.OnTabChanged -> onTabChanged(action.type)
            SearchDetailAction.OnLoadMore -> loadMore()
            SearchDetailAction.OnRetryClick -> search(resetCursor = true)
            is SearchDetailAction.Navigation -> Unit // Root에서 처리한다.
        }
    }

    private fun onSearch() {
        val query = _state.value.searchFieldState.text.toString()
        if (query.isBlank()) return
        _state.update {
            it.copy(
                animeResult = AnimeSearchResult(),
                actorResult = ActorSearchResult(),
                studioResult = StudioSearchResult(),
            )
        }
        search(resetCursor = true)
        saveRecentSearch(query)
    }

    private fun saveRecentSearch(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch { searchRepository.saveRecentSearch(query) }
    }

    private fun onTabChanged(type: SearchType) {
        if (_state.value.searchType == type) return
        _state.update { it.copy(searchType = type) }
        search(resetCursor = true)
    }

    private fun loadMore() {
        val current = _state.value
        if (current.isLoading || current.isLoadingMore || current.endReached) return
        search(resetCursor = false)
    }

    private fun search(resetCursor: Boolean) {
        when (_state.value.searchType) {
            SearchType.ANIME -> searchAnimes(resetCursor)
            SearchType.ACTOR -> searchActors(resetCursor)
            SearchType.STUDIO -> searchStudios(resetCursor)
        }
    }

    private fun searchAnimes(resetCursor: Boolean) {
        viewModelScope.launch {
            val current = _state.value
            val result = current.animeResult

            _state.update {
                if (resetCursor) it.copy(isLoading = true, error = null) else it.copy(isLoadingMore = true)
            }

            val query = current.searchFieldState.text.toString()
            val lastId = if (resetCursor) null else result.cursor?.lastId
            val page = if (resetCursor) 1L else (result.nextPage ?: 1L)

            searchRepository.getSearchAnimes(query = query, lastId = lastId, size = PAGE_SIZE, page = page)
                .onSuccess { response ->
                    val animes = response.animes.items ?: emptyList()
                    _state.update {
                        it.copy(
                            animeCount = response.counts.animeCount ?: 0,
                            actorCount = response.counts.actorCount ?: 0,
                            studioCount = response.counts.studioCount ?: 0,
                            animeResult = it.animeResult.copy(
                                animes = if (resetCursor) animes else it.animeResult.animes + animes,
                                cursor = response.animes.cursor,
                                nextPage = response.nextPage,
                            ),
                            endReached = animes.size < PAGE_SIZE || response.nextPage == null,
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

    private fun searchActors(resetCursor: Boolean) {
        viewModelScope.launch {
            val current = _state.value
            val result = current.actorResult

            _state.update {
                if (resetCursor) it.copy(isLoading = true, error = null) else it.copy(isLoadingMore = true)
            }

            val query = current.searchFieldState.text.toString()
            val lastId = if (resetCursor) null else result.cursor?.lastId

            searchRepository.getSearchActors(query = query, lastId = lastId, size = PAGE_SIZE)
                .onSuccess { response ->
                    val actors = response.actors.items ?: emptyList()
                    _state.update {
                        it.copy(
                            animeCount = response.counts.animeCount ?: 0,
                            actorCount = response.counts.actorCount ?: 0,
                            studioCount = response.counts.studioCount ?: 0,
                            actorResult = it.actorResult.copy(
                                actors = if (resetCursor) actors else it.actorResult.actors + actors,
                                cursor = response.actors.cursor,
                            ),
                            endReached = actors.size < PAGE_SIZE || response.actors.cursor == null,
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

    private fun searchStudios(resetCursor: Boolean) {
        viewModelScope.launch {
            val current = _state.value
            val result = current.studioResult

            _state.update {
                if (resetCursor) it.copy(isLoading = true, error = null) else it.copy(isLoadingMore = true)
            }

            val query = current.searchFieldState.text.toString()
            val lastId = if (resetCursor) null else result.cursor?.lastId

            searchRepository.getSearchStudios(query = query, lastId = lastId, size = PAGE_SIZE)
                .onSuccess { response ->
                    val studios = response.studios.items ?: emptyList()
                    _state.update {
                        it.copy(
                            animeCount = response.counts.animeCount ?: 0,
                            actorCount = response.counts.actorCount ?: 0,
                            studioCount = response.counts.studioCount ?: 0,
                            studioResult = it.studioResult.copy(
                                studios = if (resetCursor) studios else it.studioResult.studios + studios,
                                cursor = response.studios.cursor,
                            ),
                            endReached = studios.size < PAGE_SIZE || response.studios.cursor == null,
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
