package com.jparkbro.core.model.actor

data class Actor(
    val personId: Long,
    val name: String,
    val profileImage: String,
    val userLikedVoiceActorId: Int? = null,
)
