package com.jparkbro.search.impl.detail

sealed interface SearchDetailEvent {
    data class ShowToast(val message: String) : SearchDetailEvent
}
