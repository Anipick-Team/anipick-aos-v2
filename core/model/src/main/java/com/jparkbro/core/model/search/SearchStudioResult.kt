package com.jparkbro.core.model.search

import com.jparkbro.core.model.pagination.Cursor
import com.jparkbro.core.model.studio.Studio

/** `GET /search/studios` 검색 결과. */
data class SearchStudioResult(
    val studios: List<Studio>? = null,
    val studioCount: Int? = null,
    val animeCount: Int? = null,
    val actorCount: Int? = null,
    val cursor: Cursor? = null,
)
