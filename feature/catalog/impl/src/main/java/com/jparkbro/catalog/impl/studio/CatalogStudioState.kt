package com.jparkbro.catalog.impl.studio

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.pagination.Cursor

data class CatalogStudioState(
    val studioId: Long = 0L,
    val studioName: String? = null,
    val animes: List<Anime> = emptyList(),
    val cursor: Cursor? = null,
    val endReached: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)
