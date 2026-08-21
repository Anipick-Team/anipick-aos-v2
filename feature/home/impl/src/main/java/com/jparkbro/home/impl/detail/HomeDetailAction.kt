package com.jparkbro.home.impl.detail

sealed interface HomeDetailAction {
    data object OnBackClick : HomeDetailAction
    data class OnAnimeClick(val animeId: Long) : HomeDetailAction
    data class OnDaySelected(val day: String) : HomeDetailAction
    data class OnSortSelected(val sort: String) : HomeDetailAction
    data object OnLoadMore : HomeDetailAction
    data object OnRetryClick : HomeDetailAction
}
