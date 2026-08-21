package com.jparkbro.core.network.search.dto

import com.jparkbro.core.model.studio.Studio
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

@Serializable
data class SearchStudiosResponse(
    val count: Int? = null,
    val animeCount: Int? = null,
    val personCount: Int? = null,
    val cursor: CursorResponse? = null,
    val studios: List<SearchStudioResponse>? = null,
)

@Serializable
data class SearchStudioResponse(
    val studioId: Long,
    val name: String? = null,
)

fun SearchStudioResponse.toStudio(): Studio = Studio(
    studioId = studioId,
    name = name,
)
