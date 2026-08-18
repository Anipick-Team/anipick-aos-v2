package com.jparkbro.core.model.search

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.pagination.Cursor

/** `GET /search/animes` 검색 결과. */
data class SearchAnimeResult(
    val animes: List<Anime>,
    val animeCount: Int,
    val actorCount: Int,
    val studioCount: Int,
    val cursor: Cursor?,
    val nextPage: Long?,
)
