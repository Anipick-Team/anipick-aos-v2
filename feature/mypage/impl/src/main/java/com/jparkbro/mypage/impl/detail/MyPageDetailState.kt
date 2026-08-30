package com.jparkbro.mypage.impl.detail

import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.model.mypage.MyCommunityComment
import com.jparkbro.core.model.pagination.Cursor
import com.jparkbro.mypage.api.MyPageDetailType

data class MyPageDetailState(
    val type: MyPageDetailType,
    val totalCount: Int = 0,
    val animes: List<Anime> = emptyList(),
    val persons: List<Actor> = emptyList(),
    val myContentTab: MyContentTab = MyContentTab.POSTS,
    val posts: List<CommunityPost> = emptyList(),
    val comments: List<MyCommunityComment> = emptyList(),
    val cursor: Cursor? = null,
    val endReached: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
)

/** [MyPageDetailType.MyContent] 전용 - 내 게시글/내 댓글 탭. */
enum class MyContentTab {
    POSTS, COMMENTS
}

internal fun MyPageDetailType.title(): String = when (this) {
    MyPageDetailType.WatchList -> "볼 애니"
    MyPageDetailType.Watching -> "보는 중"
    MyPageDetailType.Finished -> "다 본 애니"
    MyPageDetailType.LikedAnimes -> "좋아요한 작품"
    MyPageDetailType.LikedPersons -> "좋아요한 인물"
    MyPageDetailType.MyContent -> "내 콘텐츠"
}

internal fun MyPageDetailType.emptyMessage(): String = when (this) {
    MyPageDetailType.WatchList -> "아직 볼 애니가 없어요."
    MyPageDetailType.Watching -> "아직 보는 중인 애니가 없어요."
    MyPageDetailType.Finished -> "아직 다 본 애니가 없어요."
    MyPageDetailType.LikedAnimes -> "아직 좋아요한 작품이 없어요.\n좋아요를 누르러 가볼까요 ?"
    MyPageDetailType.LikedPersons -> "아직 좋아요한 인물이 없어요.\n좋아요를 누르러 가볼까요 ?"
    MyPageDetailType.MyContent -> ""
}
