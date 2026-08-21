package com.jparkbro.core.model.anime

import com.jparkbro.core.model.pagination.Cursor

data class SeriesResult(
    val count: Int? = null,
    val cursor: Cursor? = null,
    val animes: List<Anime>? = null,
)
