package com.jparkbro.home.impl.detail

sealed interface HomeDetailAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : HomeDetailAction

    data object OnBackClick : Navigation
    data class OnAnimeClick(val animeId: Long) : Navigation
    data class OnDaySelected(val day: String) : HomeDetailAction
    data class OnSortSelected(val sort: String) : HomeDetailAction
    data object OnLoadMore : HomeDetailAction
    data object OnRetryClick : HomeDetailAction
}
