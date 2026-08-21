package com.jparkbro.core.model.community

import com.jparkbro.core.model.pagination.Cursor

data class CommunityBoardsResult(
    val count: Int? = null,
    val cursor: Cursor? = null,
    val boards: List<CommunityBoard>? = null,
)
