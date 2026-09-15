package com.jparkbro.community.impl.main

import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.model.pagination.Cursor

data class CommunityMainState(
    val seriesId: Long,
    /** 진입 시 넘겨받은 [CommunityBoard][com.jparkbro.core.model.community.CommunityBoard] 값 그대로 -
     *  별도로 다시 조회하지 않는다. */
    val boardTitle: String? = null,
    val boardCoverImageUrl: String? = null,
    val boardGenres: List<String> = emptyList(),
    val postFilter: CommunityPostFilter = CommunityPostFilter.ALL,
    val isSpoilerVisible: Boolean = false,
    val posts: List<CommunityPost> = emptyList(),
    val postsCursor: Cursor? = null,
    val postsEndReached: Boolean = false,
    val isPostsLoading: Boolean = false,
    val isLoadingMorePosts: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    /** 스포일러 노출 스위치가 꺼져있으면 스포일러 게시글은 목록에서 뺀다 - 서버는 이 필터를 모르고
     *  전체를 내려주므로 클라이언트에서만 걸러서 보여준다. */
    val visiblePosts: List<CommunityPost>
        get() = if (isSpoilerVisible) posts else posts.filterNot { it.isSpoiler == true }
}

enum class CommunityPostFilter {
    ALL,
    MONTHLY,
    WEEKLY,
    DAILY,
}

internal val CommunityPostFilter.sortParam: String
    get() = when (this) {
        CommunityPostFilter.ALL -> "latest"
        CommunityPostFilter.MONTHLY -> "popularMonthly"
        CommunityPostFilter.WEEKLY -> "popularWeekly"
        CommunityPostFilter.DAILY -> "popularDaily"
    }
