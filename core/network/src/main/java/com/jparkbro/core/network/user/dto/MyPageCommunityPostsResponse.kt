package com.jparkbro.core.network.user.dto

import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.network.BuildConfig
import com.jparkbro.core.network.common.CursorResponse
import kotlinx.serialization.Serializable

/** 마이페이지 "내가 쓴 게시글" 목록 응답 */
@Serializable
data class MyPageCommunityPostsResponse(
    val count: Int? = null,
    val cursor: CursorResponse? = null,
    val posts: List<MyPageCommunityPostResponse>? = null,
)

@Serializable
data class MyPageCommunityPostResponse(
    val postId: Long,
    val seriesId: Long? = null,
    val animeTitle: String? = null,
    val animeCoverImageUrl: String? = null,
    val title: String? = null,
    val content: String? = null,
    val thumbnailImageId: Long? = null,
    val isSpoiler: Boolean? = null,
    val viewCount: Int? = null,
    val likeCount: Int? = null,
    val commentCount: Int? = null,
    val createdAt: String? = null,
)

fun MyPageCommunityPostResponse.toCommunityPost(): CommunityPost = CommunityPost(
    postId = postId,
    seriesId = seriesId,
    seriesTitle = animeTitle,
    animeCoverImageUrl = animeCoverImageUrl,
    title = title,
    content = content,
    thumbnailImageUrl = thumbnailImageId?.let { "${BuildConfig.BASE_URL}/image/$it" },
    isSpoiler = isSpoiler,
    viewCount = viewCount,
    likeCount = likeCount,
    commentCount = commentCount,
    isMine = true,
    createdAt = createdAt,
)
