package com.jparkbro.core.network.search.dto

import com.jparkbro.core.model.studio.Studio
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

@Serializable
data class SearchStudiosResponse(
    val count: Int = 0,
    val animeCount: Int = 0,
    val personCount: Int = 0,
    val cursor: CursorResponse = CursorResponse(),
    val studios: List<SearchStudioResponse> = emptyList(),
)

@Serializable
data class SearchStudioResponse(
    val studioId: Long,
    val name: String,
)

fun SearchStudioResponse.toStudio(): Studio = Studio(
    studioId = studioId,
    name = name,
)
