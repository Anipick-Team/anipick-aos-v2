package com.jparkbro.core.network.common

import com.jparkbro.core.model.Genre
import com.jparkbro.core.model.Metadata
import com.jparkbro.core.model.Season
import kotlinx.serialization.Serializable

@Serializable
data class MetaDataResponse(
    val seasonYear: List<Int> = emptyList(),
    val season: List<MetadataItemResponse> = emptyList(),
    val genres: List<MetadataItemResponse> = emptyList(),
    val type: List<String> = emptyList(),
)

@Serializable
data class MetadataItemResponse(
    val id: Int,
    val name: String,
)

fun MetaDataResponse.toMetadata(): Metadata = Metadata(
    seasonYears = seasonYear,
    seasons = season.map { Season(id = it.id, name = it.name) },
    genres = genres.map { Genre(id = it.id, name = it.name) },
    types = type,
)
