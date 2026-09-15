package com.jparkbro.core.network.community.dto

import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.network.common.CursorResponse
import com.jparkbro.core.network.image.toImageUrl
import kotlinx.serialization.Serializable

@Serializable
data class CommunityPostsResponse(
    val count: Int? = null,
    val cursor: CursorResponse? = null,
    val posts: List<CommunityPostResponse>? = null,
)

@Serializable
data class CommunityPostResponse(
    val postId: Long,
    val userId: Long? = null,
    val nickname: String? = null,
    val profileImageId: Long? = null,
    val title: String? = null,
    val content: String? = null,
    val thumbnailImageId: Long? = null,
    val imageCount: Int? = null,
    val isSpoiler: Boolean? = null,
    val viewCount: Int? = null,
    val likeCount: Int? = null,
    val commentCount: Int? = null,
    val createdAt: String? = null,
)

fun CommunityPostResponse.toCommunityPost(): CommunityPost = CommunityPost(
    postId = postId,
    userId = userId,
    nickname = nickname,
    profileImageUrl = profileImageId?.toImageUrl(),
    title = title,
    content = content,
    thumbnailImageUrl = thumbnailImageId?.toImageUrl(),
    imageCount = imageCount,
    isSpoiler = isSpoiler,
    viewCount = viewCount,
    likeCount = likeCount,
    commentCount = commentCount,
    createdAt = createdAt,
)
