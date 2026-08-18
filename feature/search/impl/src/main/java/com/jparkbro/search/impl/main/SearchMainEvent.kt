package com.jparkbro.search.impl.main

sealed interface SearchMainEvent {
    data class NavigateToDetail(val query: String) : SearchMainEvent
    data class ShowToast(val message: String) : SearchMainEvent
}
