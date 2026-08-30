package com.jparkbro.core.model.search

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.pagination.CursorPage

/** `GET /search/animes` 검색 결과. */
data class SearchAnimePage(
    val animes: CursorPage<Anime> = CursorPage(),
    val counts: SearchCounts = SearchCounts(),
    val nextPage: Long? = null,
)
