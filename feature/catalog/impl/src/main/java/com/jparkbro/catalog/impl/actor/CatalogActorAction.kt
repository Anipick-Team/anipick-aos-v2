package com.jparkbro.catalog.impl.actor

sealed interface CatalogActorAction {

    /** Root에서 처리하는 화면 이탈 액션(앱 내 이동 + 외부 인텐트) - ViewModel로 내려가지 않는다. */
    sealed interface Navigation : CatalogActorAction

    data object OnBackClick : Navigation
    data class OnAnimeClick(val animeId: Long) : Navigation
    data object OnLoadMore : CatalogActorAction
}
