package com.jparkbro.core.model.search

import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.pagination.Cursor

/** `GET /search/persons` 검색 결과. */
data class SearchActorResult(
    val actors: List<Actor>,
    val actorCount: Int,
    val animeCount: Int,
    val studioCount: Int,
    val cursor: Cursor?,
)
