package com.jparkbro.search.impl.main

import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.search.SearchRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchMainViewModel(
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SearchMainState())
    val state: StateFlow<SearchMainState> = _state.asStateFlow()

    private val _events = Channel<SearchMainEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadPopularAnimes()
        observeRecentSearches()
    }

    fun onAction(action: SearchMainAction) {
        when (action) {
            SearchMainAction.OnSearch -> search()
            SearchMainAction.OnSearchClearClick -> _state.value.searchFieldState.clearText()
            is SearchMainAction.OnRecentSearchRemove -> removeRecentSearch(action.query)
            SearchMainAction.OnRecentSearchClearAll -> clearRecentSearches()
            SearchMainAction.OnRetryClick -> loadPopularAnimes()
            SearchMainAction.OnBackClick,
            is SearchMainAction.OnRecentSearchClick,
            is SearchMainAction.OnAnimeClick,
            -> Unit // 네비게이션만 필요한 액션은 Root에서 처리한다.
        }
    }

    private fun observeRecentSearches() {
        searchRepository.recentSearches
            .onEach { recentSearches -> _state.update { it.copy(recentSearches = recentSearches) } }
            .launchIn(viewModelScope)
    }

    private fun removeRecentSearch(query: String) {
        viewModelScope.launch { searchRepository.removeRecentSearch(query) }
    }

    private fun clearRecentSearches() {
        viewModelScope.launch { searchRepository.clearRecentSearches() }
    }

    private fun search() {
        val query = _state.value.searchFieldState.text.toString()
        if (query.isBlank()) {
            sendEvent(SearchMainEvent.ShowToast("검색어를 입력해주세요"))
        } else {
            sendEvent(SearchMainEvent.NavigateToDetail(query))
        }
    }

    private fun sendEvent(event: SearchMainEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }

    private fun loadPopularAnimes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            searchRepository.getPopularAnimes()
                .onSuccess { animes -> _state.update { it.copy(animes = animes ?: emptyList(), isLoading = false) } }
                .onFailure { error -> _state.update { it.copy(isLoading = false, error = error.toDisplayMessage()) } }
        }
    }
}
