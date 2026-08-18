package com.jparkbro.search.impl.detail

sealed interface SearchDetailAction {
    data object OnSearch : SearchDetailAction
    data object OnSearchClearClick : SearchDetailAction
    data class OnTabChanged(val type: SearchType) : SearchDetailAction
    data object OnLoadMore : SearchDetailAction
    data object OnBackClick : SearchDetailAction
    data class OnAnimeClick(val animeId: Long) : SearchDetailAction
    data class OnActorClick(val personId: Long) : SearchDetailAction
    data class OnStudioClick(val studioId: Long) : SearchDetailAction
}
