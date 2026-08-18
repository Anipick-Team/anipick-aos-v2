package com.jparkbro.core.network.search.dto

import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

@Serializable
data class SearchPersonsResponse(
    val count: Int = 0,
    val animeCount: Int = 0,
    val studioCount: Int = 0,
    val cursor: CursorResponse = CursorResponse(),
    val persons: List<SearchPersonResponse> = emptyList(),
)

@Serializable
data class SearchPersonResponse(
    val personId: Long,
    val name: String,
    val profileImage: String,
)

fun SearchPersonResponse.toActor(): Actor = Actor(
    personId = personId,
    name = name,
    profileImage = profileImage,
)
