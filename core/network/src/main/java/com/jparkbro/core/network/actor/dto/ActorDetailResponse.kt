package com.jparkbro.core.network.actor.dto

import com.jparkbro.core.model.actor.ActorDetailPage
import com.jparkbro.core.model.actor.ActorWork
import com.jparkbro.core.network.common.CursorResponse
import com.jparkbro.core.network.common.toCursor
import kotlinx.serialization.Serializable

@Serializable
data class ActorDetailResponse(
    val personId: Long,
    val name: String? = null,
    val profileImageUrl: String? = null,
    val isLiked: Boolean? = null,
    val count: Int? = null,
    val cursor: CursorResponse? = null,
    val works: List<ActorWorkResponse>? = null,
)

@Serializable
data class ActorWorkResponse(
    val animeId: Long,
    val animeTitle: String? = null,
    val characterId: Long,
    val characterName: String? = null,
    val characterImageUrl: String? = null,
)

fun ActorWorkResponse.toActorWork(): ActorWork = ActorWork(
    animeId = animeId,
    animeTitle = animeTitle,
    characterId = characterId,
    characterName = characterName,
    characterImageUrl = characterImageUrl,
)

fun ActorDetailResponse.toActorDetailPage(): ActorDetailPage = ActorDetailPage(
    personId = personId,
    name = name,
    profileImageUrl = profileImageUrl,
    isLiked = isLiked,
    count = count,
    cursor = cursor.toCursor(),
    works = works?.map { it.toActorWork() },
)
