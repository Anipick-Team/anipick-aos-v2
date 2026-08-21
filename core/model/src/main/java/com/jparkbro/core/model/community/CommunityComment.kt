package com.jparkbro.core.model.community

/** 커뮤니티 게시글 댓글 한 건. 대댓글이면 [replies]에 담긴다. */
data class CommunityComment(
    val commentId: Long,
    val userId: Long? = null,
    val nickname: String? = null,
    val profileImageUrl: String? = null,
    val content: String? = null,
    val likeCount: Int? = null,
    val isLiked: Boolean? = null,
    val isMine: Boolean? = null,
    val isDeleted: Boolean? = null,
    val isEdited: Boolean? = null,
    val createdAt: String? = null,
    val replies: List<CommunityComment>? = null,
)
