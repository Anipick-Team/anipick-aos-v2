package com.jparkbro.core.network.community.dto

import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.network.BuildConfig
import kotlinx.serialization.Serializable

@Serializable
data class CommunityPostDetailResponse(
    val postId: Long,
    val seriesId: Long? = null,
    val seriesTitle: String? = null,
    val userId: Long? = null,
    val nickname: String? = null,
    val profileImageId: Long? = null,
    val title: String? = null,
    val content: String? = null,
    val isSpoiler: Boolean? = null,
    val imageIds: List<Long>? = null,
    val viewCount: Int? = null,
    val likeCount: Int? = null,
    val commentCount: Int? = null,
    val isLiked: Boolean? = null,
    val isMine: Boolean? = null,
    val isAuthorBlocked: Boolean? = null,
    val createdAt: String? = null,
)

fun CommunityPostDetailResponse.toCommunityPost(): CommunityPost = CommunityPost(
    postId = postId,
    seriesId = seriesId,
    seriesTitle = seriesTitle,
    userId = userId,
    nickname = nickname,
    profileImageUrl = profileImageId?.let { "${BuildConfig.BASE_URL}/image/$it" },
    title = title,
    content = content,
    imageUrls = imageIds?.map { "${BuildConfig.BASE_URL}/image/$it" },
    isSpoiler = isSpoiler,
    viewCount = viewCount,
    likeCount = likeCount,
    commentCount = commentCount,
    isLiked = isLiked,
    isMine = isMine,
    isAuthorBlocked = isAuthorBlocked,
    createdAt = createdAt,
)
