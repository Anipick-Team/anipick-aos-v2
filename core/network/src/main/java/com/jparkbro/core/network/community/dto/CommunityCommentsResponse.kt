package com.jparkbro.core.network.community.dto

import com.jparkbro.core.model.community.CommunityComment
import com.jparkbro.core.network.BuildConfig
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

@Serializable
data class CommunityCommentsResponse(
    val count: Int? = null,
    val cursor: CursorResponse? = null,
    val comments: List<CommunityCommentResponse>? = null,
)

@Serializable
data class CommunityCommentResponse(
    val commentId: Long,
    val userId: Long? = null,
    val nickname: String? = null,
    val profileImageId: Long? = null,
    val content: String? = null,
    val likeCount: Int? = null,
    val isLiked: Boolean? = null,
    val isMine: Boolean? = null,
    val isDeleted: Boolean? = null,
    val isEdited: Boolean? = null,
    val createdAt: String? = null,
    val replies: List<CommunityCommentResponse>? = null,
)

fun CommunityCommentResponse.toCommunityComment(): CommunityComment = CommunityComment(
    commentId = commentId,
    userId = userId,
    nickname = nickname,
    profileImageUrl = profileImageId?.let { "${BuildConfig.BASE_URL}/image/$it" },
    content = content,
    likeCount = likeCount,
    isLiked = isLiked,
    isMine = isMine,
    isDeleted = isDeleted,
    isEdited = isEdited,
    createdAt = createdAt,
    replies = replies?.map { it.toCommunityComment() },
)
