package com.jparkbro.core.model.character

data class Character(
    val characterId: Long,
    val name: String? = null,
    val imageUrl: String? = null,
)
