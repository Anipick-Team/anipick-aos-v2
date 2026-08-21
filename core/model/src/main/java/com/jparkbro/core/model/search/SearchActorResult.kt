package com.jparkbro.core.model.search

import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.pagination.Cursor

/** `GET /search/persons` 검색 결과. */
data class SearchActorResult(
    val actors: List<Actor>? = null,
    val actorCount: Int? = null,
    val animeCount: Int? = null,
    val studioCount: Int? = null,
    val cursor: Cursor? = null,
)
