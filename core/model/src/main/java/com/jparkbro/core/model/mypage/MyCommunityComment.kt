package com.jparkbro.core.model.mypage

/** 마이페이지 "내가 쓴 댓글" 목록 한 건 - 댓글/대댓글 공통, 원글 맥락만 담는다. */
data class MyCommunityComment(
    val commentId: Long,
    val postId: Long? = null,
    val animeTitle: String? = null,
    val animeCoverImageUrl: String? = null,
    val postTitle: String? = null,
    val content: String? = null,
    val likeCount: Int? = null,
    val createdAt: String? = null,
)
