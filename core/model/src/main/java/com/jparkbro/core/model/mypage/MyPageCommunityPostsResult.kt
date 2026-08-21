package com.jparkbro.core.model.mypage

import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.model.pagination.Cursor

data class MyPageCommunityPostsResult(
    val count: Int? = null,
    val cursor: Cursor? = null,
    val posts: List<CommunityPost>? = null,
)
