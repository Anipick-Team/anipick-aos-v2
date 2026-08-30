package com.jparkbro.search.impl.main

sealed interface SearchMainAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : SearchMainAction

    data object OnSearch : SearchMainAction
    data object OnSearchClearClick : SearchMainAction
    data class OnRecentSearchClick(val query: String) : Navigation
    data class OnRecentSearchRemove(val query: String) : SearchMainAction
    data object OnRecentSearchClearAll : SearchMainAction
    data object OnRetryClick : SearchMainAction
    data object OnBackClick : Navigation
    data class OnAnimeClick(val animeId: Long) : Navigation
}
