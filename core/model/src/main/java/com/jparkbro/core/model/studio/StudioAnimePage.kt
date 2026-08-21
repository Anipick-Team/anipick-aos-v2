package com.jparkbro.core.model.studio

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.pagination.Cursor

data class StudioAnimePage(
    val studioName: String? = null,
    val cursor: Cursor? = null,
    val animes: List<Anime>? = null,
)
