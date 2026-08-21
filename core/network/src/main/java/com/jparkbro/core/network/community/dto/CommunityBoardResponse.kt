package com.jparkbro.core.network.community.dto

import com.jparkbro.core.model.community.CommunityBoard
import com.jparkbro.core.model.metadata.Genre
import com.jparkbro.core.network.common.MetadataItemResponse
import kotlinx.serialization.Serializable

@Serializable
data class CommunityBoardResponse(
    val hasBoard: Boolean,
    val seriesId: Long? = null,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val genres: List<MetadataItemResponse>? = null,
    val postCount: Int? = null,
)

fun CommunityBoardResponse.toCommunityBoard(): CommunityBoard = CommunityBoard(
    hasBoard = hasBoard,
    seriesId = seriesId,
    title = title,
    coverImageUrl = coverImageUrl,
    genres = genres?.map { Genre(id = it.id, name = it.name) },
    postCount = postCount,
)
