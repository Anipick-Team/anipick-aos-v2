package com.jparkbro.core.network.common

import com.jparkbro.core.model.metadata.Genre
import com.jparkbro.core.model.metadata.Metadata
import com.jparkbro.core.model.metadata.Season
import kotlinx.serialization.Serializable

@Serializable
data class MetaDataResponse(
    val seasonYear: List<Int>? = null,
    val season: List<MetadataItemResponse>? = null,
    val genres: List<MetadataItemResponse>? = null,
    val type: List<String>? = null,
)

@Serializable
data class MetadataItemResponse(
    val id: Int,
    val name: String? = null,
)

fun MetaDataResponse.toMetadata(): Metadata = Metadata(
    seasonYears = seasonYear,
    seasons = season?.map { Season(id = it.id, name = it.name) },
    genres = genres?.map { Genre(id = it.id, name = it.name) },
    types = type,
)
