package com.jparkbro.core.model.mypage

import com.jparkbro.core.model.pagination.Cursor

data class MyPageCommunityCommentsResult(
    val count: Int? = null,
    val cursor: Cursor? = null,
    val comments: List<MyCommunityComment>? = null,
)
