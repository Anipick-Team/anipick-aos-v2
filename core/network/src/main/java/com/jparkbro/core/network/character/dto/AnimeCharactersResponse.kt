package com.jparkbro.core.network.character.dto

import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.character.AnimeCharacter
import com.jparkbro.core.model.character.Character
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

@Serializable
data class AnimeCharactersResponse(
    val cursor: CursorResponse? = null,
    val characters: List<AnimeCharacterResponse>? = null,
)

@Serializable
data class AnimeCharacterResponse(
    val character: CharacterResponse,
    val voiceActor: VoiceActorResponse? = null,
    val role: String? = null,
)

@Serializable
data class CharacterResponse(
    val id: Long,
    val name: String? = null,
    val imageUrl: String? = null,
)

@Serializable
data class VoiceActorResponse(
    val id: Long,
    val name: String? = null,
    val imageUrl: String? = null,
)

fun AnimeCharacterResponse.toAnimeCharacter(): AnimeCharacter = AnimeCharacter(
    character = character.toCharacter(),
    voiceActor = voiceActor?.toActor(),
    role = role,
)

fun CharacterResponse.toCharacter(): Character = Character(
    characterId = id,
    name = name,
    imageUrl = imageUrl,
)

fun VoiceActorResponse.toActor(): Actor = Actor(
    personId = id,
    name = name,
    profileImage = imageUrl,
)
