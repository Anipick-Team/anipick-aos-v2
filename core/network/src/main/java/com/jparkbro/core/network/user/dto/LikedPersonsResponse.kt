package com.jparkbro.core.network.user.dto

import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

/** 마이페이지 "찜한 인물" 목록 응답 */
@Serializable
data class LikedPersonsResponse(
    val count: Int? = null,
    val cursor: CursorResponse? = null,
    val persons: List<LikedPersonResponse>? = null,
)
