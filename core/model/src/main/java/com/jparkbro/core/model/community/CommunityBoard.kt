package com.jparkbro.core.model.community

import com.jparkbro.core.model.metadata.Genre

/** 애니 기준 커뮤니티 게시판 존재 여부 - `GET /community/boards/by-anime/{animeId}`. */
data class CommunityBoard(
    val hasBoard: Boolean,
    val seriesId: Long? = null,
    val title: String? = null,
    val coverImageUrl: String? = null,
    val genres: List<Genre>? = null,
    val postCount: Int? = null,
)
