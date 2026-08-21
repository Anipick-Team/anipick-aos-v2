package com.jparkbro.core.model.community

/** 커뮤니티 게시판 게시글 한 건. [seriesId]/[seriesTitle]/[imageUrls]/[isLiked]/[isMine]/[isAuthorBlocked]는
 *  상세 조회에서만, [animeCoverImageUrl]은 마이페이지 "내가 쓴 게시글" 목록에서만 채워진다. */
data class CommunityPost(
    val postId: Long,
    val seriesId: Long? = null,
    val seriesTitle: String? = null,
    val animeCoverImageUrl: String? = null,
    val userId: Long? = null,
    val nickname: String? = null,
    val profileImageUrl: String? = null,
    val title: String? = null,
    val content: String? = null,
    val thumbnailImageUrl: String? = null,
    val imageUrls: List<String>? = null,
    val isSpoiler: Boolean? = null,
    val viewCount: Int? = null,
    val likeCount: Int? = null,
    val commentCount: Int? = null,
    val isLiked: Boolean? = null,
    val isMine: Boolean? = null,
    val isAuthorBlocked: Boolean? = null,
    val createdAt: String? = null,
)
