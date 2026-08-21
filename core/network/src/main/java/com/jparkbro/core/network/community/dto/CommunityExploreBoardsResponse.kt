package com.jparkbro.core.network.community.dto

import com.jparkbro.core.model.community.CommunityBoard
import com.jparkbro.core.model.metadata.Genre
import com.jparkbro.core.network.common.CursorResponse
import com.jparkbro.core.network.common.MetadataItemResponse
import kotlinx.serialization.Serializable

@Serializable
data class CommunityExploreBoardsResponse(
    val count: Int? = null,
    val cursor: CursorResponse? = null,
    val boards: List<CommunityExploreBoardResponse>? = null,
)

@Serializable
data class CommunityExploreBoardResponse(
    val seriesId: Long,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val genres: List<MetadataItemResponse>? = null,
)

fun CommunityExploreBoardResponse.toCommunityBoard(): CommunityBoard = CommunityBoard(
    hasBoard = true,
    seriesId = seriesId,
    title = title,
    coverImageUrl = coverImageUrl,
    genres = genres?.map { Genre(id = it.id, name = it.name) },
)
