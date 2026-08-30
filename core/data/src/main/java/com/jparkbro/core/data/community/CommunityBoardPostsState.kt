package com.jparkbro.core.data.community

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.model.pagination.Cursor

/** 현재 열려 있는 커뮤니티 게시판의 게시글 목록 캐시 - [CommunityRepository.communityBoardPosts]로 노출된다.
 *  화면 하나만 떠 있다고 가정한 단일 캐시라 [seriesId]로 어느 게시판 것인지 구분한다. 게시판 화면을
 *  벗어나면([CommunityRepository.clearCommunityBoardPosts]) 비워져서, 다른 게시판에 새로 들어가면
 *  항상 빈 상태([seriesId] == null)로 시작한다. */
data class CommunityBoardPostsState(
    val seriesId: Long? = null,
    val sort: String? = null,
    val posts: List<CommunityPost> = emptyList(),
    val cursor: Cursor? = null,
    val endReached: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: DataError.Network? = null,
)
