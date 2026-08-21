package com.jparkbro.core.network.search.dto

import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

@Serializable
data class SearchPersonsResponse(
    val count: Int? = null,
    val animeCount: Int? = null,
    val studioCount: Int? = null,
    val cursor: CursorResponse? = null,
    val persons: List<SearchPersonResponse>? = null,
)

@Serializable
data class SearchPersonResponse(
    val personId: Long,
    val name: String? = null,
    val profileImage: String? = null,
)

fun SearchPersonResponse.toActor(): Actor = Actor(
    personId = personId,
    name = name,
    profileImage = profileImage,
)
