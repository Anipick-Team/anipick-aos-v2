package com.jparkbro.core.network.community.dto

import kotlinx.serialization.Serializable

/** 게시판 게시글 목록 커서 페이지네이션 요청. [sort]는 "latest"/"popularDaily"/"popularWeekly"/"popularMonthly" 중 하나. */
@Serializable
data class CommunityPostsRequest(
    val sort: String? = null,
    val lastId: Long? = null,
    val lastValue: String? = null,
    val size: Int = 20,
)
