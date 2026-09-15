package com.jparkbro.core.data.user

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.model.mypage.MyCommunityComment
import com.jparkbro.core.model.pagination.Cursor

/** 내가 쓴 댓글 목록 캐시 - [UserRepository.myCommunityComments]로 노출된다 */
data class MyCommunityCommentsState(
    val comments: List<MyCommunityComment> = emptyList(),
    val cursor: Cursor? = null,
    val totalCount: Int? = null,
    val endReached: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: DataError.Network? = null,
)
