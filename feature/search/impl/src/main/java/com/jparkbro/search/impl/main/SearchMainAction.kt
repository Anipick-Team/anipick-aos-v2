package com.jparkbro.search.impl.main

sealed interface SearchMainAction {
    data object OnSearch : SearchMainAction
    data object OnSearchClearClick : SearchMainAction
    data class OnRecentSearchClick(val query: String) : SearchMainAction
    data class OnRecentSearchRemove(val query: String) : SearchMainAction
    data object OnRecentSearchClearAll : SearchMainAction
    data object OnRetryClick : SearchMainAction
    data object OnBackClick : SearchMainAction
    data class OnAnimeClick(val animeId: Long) : SearchMainAction
}
