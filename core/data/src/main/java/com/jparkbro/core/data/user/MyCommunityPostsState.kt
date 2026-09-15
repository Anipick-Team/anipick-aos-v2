package com.jparkbro.core.data.user

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.model.pagination.Cursor

/** 내가 쓴 게시글 목록 캐시 - [UserRepository.myCommunityPosts]로 노출된다 */
data class MyCommunityPostsState(
    val posts: List<CommunityPost> = emptyList(),
    val cursor: Cursor? = null,
    val totalCount: Int? = null,
    val endReached: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: DataError.Network? = null,
)
