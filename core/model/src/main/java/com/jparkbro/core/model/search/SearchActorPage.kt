package com.jparkbro.core.model.search

import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.pagination.CursorPage

/** `GET /search/persons` 검색 결과. */
data class SearchActorPage(
    val actors: CursorPage<Actor> = CursorPage(),
    val counts: SearchCounts = SearchCounts(),
)
