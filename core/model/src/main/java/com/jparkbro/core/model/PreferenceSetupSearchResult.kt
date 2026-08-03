package com.jparkbro.core.model

data class PreferenceSetupSearchResult(
    val count: Int,
    val cursor: Cursor,
    val animes: List<Anime>,
)
