package com.jparkbro.core.model.character

import com.jparkbro.core.model.actor.Actor

data class AnimeCharacter(
    val character: Character,
    val voiceActor: Actor? = null,
    val role: String? = null,
)
