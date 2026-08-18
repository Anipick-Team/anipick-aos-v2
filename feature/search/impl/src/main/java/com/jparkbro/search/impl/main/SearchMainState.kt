package com.jparkbro.search.impl.main

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.core.model.anime.Anime

data class SearchMainState(
    val searchFieldState: TextFieldState = TextFieldState(),
    val recentSearches: List<String> = emptyList(),
    val animes: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
