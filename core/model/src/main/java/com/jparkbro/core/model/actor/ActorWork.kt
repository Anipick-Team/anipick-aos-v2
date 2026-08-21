package com.jparkbro.core.model.actor

data class ActorWork(
    val animeId: Long,
    val animeTitle: String? = null,
    val characterId: Long,
    val characterName: String? = null,
    val characterImageUrl: String? = null,
)
