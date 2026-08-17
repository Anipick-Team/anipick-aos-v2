package com.jparkbro.core.network.anime.dto

import kotlinx.serialization.Serializable

@Serializable
data class PreferenceSetupSearchRequest(
    val query: String? = null,
    val year: String? = null,
    val season: Int? = null,
    val genres: Int? = null,
    val lastId: Long? = null,
    val size: Int? = 10,
)
