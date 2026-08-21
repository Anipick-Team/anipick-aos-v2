package com.jparkbro.core.network.community.dto

import kotlinx.serialization.Serializable

/** 커뮤니티 탐색 게시판 목록 커서 페이지네이션 요청. [sort]는 "popular"/"latest" 중 하나. */
@Serializable
data class CommunityExploreBoardsRequest(
    val sort: String? = null,
    val keyword: String? = null,
    val lastId: Long? = null,
    val lastValue: String? = null,
    val size: Int = 20,
)
