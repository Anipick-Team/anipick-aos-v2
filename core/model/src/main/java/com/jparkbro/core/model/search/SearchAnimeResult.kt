package com.jparkbro.core.model.search

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.pagination.Cursor

/** `GET /search/animes` 검색 결과. */
data class SearchAnimeResult(
    val animes: List<Anime>? = null,
    val animeCount: Int? = null,
    val actorCount: Int? = null,
    val studioCount: Int? = null,
    val cursor: Cursor? = null,
    val nextPage: Long? = null,
)
