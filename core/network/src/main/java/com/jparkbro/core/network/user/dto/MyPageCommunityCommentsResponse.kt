package com.jparkbro.core.network.user.dto

import com.jparkbro.core.model.mypage.MyCommunityComment
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

/** 마이페이지 "내가 쓴 댓글" 목록 응답 */
@Serializable
data class MyPageCommunityCommentsResponse(
    val count: Int? = null,
    val cursor: CursorResponse? = null,
    val comments: List<MyPageCommunityCommentResponse>? = null,
)

@Serializable
data class MyPageCommunityCommentResponse(
    val commentId: Long,
    val postId: Long? = null,
    val animeTitle: String? = null,
    val animeCoverImageUrl: String? = null,
    val postTitle: String? = null,
    val content: String? = null,
    val likeCount: Int? = null,
    val createdAt: String? = null,
)

fun MyPageCommunityCommentResponse.toMyCommunityComment(): MyCommunityComment = MyCommunityComment(
    commentId = commentId,
    postId = postId,
    animeTitle = animeTitle,
    animeCoverImageUrl = animeCoverImageUrl,
    postTitle = postTitle,
    content = content,
    likeCount = likeCount,
    createdAt = createdAt,
)
