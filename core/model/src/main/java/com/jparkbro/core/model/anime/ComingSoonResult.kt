package com.jparkbro.core.model.anime

import com.jparkbro.core.model.pagination.Cursor

data class ComingSoonResult(
    val count: Int,
    val cursor: Cursor,
    val animes: List<Anime> = emptyList(),
)
