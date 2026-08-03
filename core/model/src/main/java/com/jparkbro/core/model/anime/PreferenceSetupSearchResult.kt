package com.jparkbro.core.model.anime

import com.jparkbro.core.model.pagination.Cursor

data class PreferenceSetupSearchResult(
    val count: Int,
    val cursor: Cursor,
    val animes: List<Anime>,
)
