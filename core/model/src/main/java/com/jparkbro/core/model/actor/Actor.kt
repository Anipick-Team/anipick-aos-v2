package com.jparkbro.core.model.actor

data class Actor(
    val personId: Long,
    val name: String? = null,
    val profileImage: String? = null,
    val userLikedVoiceActorId: Int? = null,
)
