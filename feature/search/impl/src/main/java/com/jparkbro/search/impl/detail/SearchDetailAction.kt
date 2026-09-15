package com.jparkbro.search.impl.detail

sealed interface SearchDetailAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : SearchDetailAction

    data object OnSearch : SearchDetailAction
    data object OnSearchClearClick : SearchDetailAction
    data class OnTabChanged(val type: SearchType) : SearchDetailAction
    data object OnLoadMore : SearchDetailAction
    data object OnRetryClick : SearchDetailAction
    data object OnBackClick : Navigation
    data class OnAnimeClick(val animeId: Long) : Navigation
    data class OnActorClick(val personId: Long) : Navigation
    data class OnStudioClick(val studioId: Long) : Navigation
    data class OnAnimeClickLog(val url: String) : SearchDetailAction
    data class OnAnimeImpressionLog(val url: String) : SearchDetailAction
}
