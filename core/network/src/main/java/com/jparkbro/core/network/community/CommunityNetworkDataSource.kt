package com.jparkbro.core.network.community

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.report.ReportCategory
import com.jparkbro.core.model.report.ReportTargetType
import com.jparkbro.core.network.community.dto.CommunityBoardResponse
import com.jparkbro.core.network.community.dto.CommunityCommentsResponse
import com.jparkbro.core.network.community.dto.CommunityExploreBoardsRequest
import com.jparkbro.core.network.community.dto.CommunityExploreBoardsResponse
import com.jparkbro.core.network.community.dto.CommunityPostDetailResponse
import com.jparkbro.core.network.community.dto.CommunityPostsRequest
import com.jparkbro.core.network.community.dto.CommunityPostsResponse
import com.jparkbro.core.network.community.dto.CreateCommentResponse
import com.jparkbro.core.network.community.dto.CreatePostResponse

interface CommunityNetworkDataSource {
    /** 애니 기준 커뮤니티 게시판 존재 여부 - `GET /community/boards/by-anime/{animeId}`. */
    suspend fun getCommunityBoardByAnime(animeId: Long): Result<CommunityBoardResponse, DataError.Network>

    /** 커뮤니티 탐색 게시판 목록 - `GET /community/explore/boards`. */
    suspend fun getExploreCommunityBoards(
        request: CommunityExploreBoardsRequest,
    ): Result<CommunityExploreBoardsResponse, DataError.Network>

    /** 커뮤니티 게시판 게시글 목록 - `GET /community/boards/{seriesId}/posts`. */
    suspend fun getCommunityPosts(
        seriesId: Long,
        request: CommunityPostsRequest,
    ): Result<CommunityPostsResponse, DataError.Network>

    /** 게시글 등록 - `POST /community/posts`. */
    suspend fun createPost(
        seriesId: Long,
        title: String,
        content: String,
        isSpoiler: Boolean,
        imageIds: List<Long>,
    ): Result<CreatePostResponse, DataError.Network>

    /** 게시글 상세 - `GET /community/posts/{postId}`. */
    suspend fun getPostDetail(postId: Long): Result<CommunityPostDetailResponse, DataError.Network>

    /** 게시글 수정 - `PATCH /community/posts/{postId}`. */
    suspend fun updatePost(
        postId: Long,
        title: String,
        content: String,
        isSpoiler: Boolean,
        imageIds: List<Long>,
    ): Result<Unit, DataError.Network>

    /** 게시글 삭제 - `DELETE /community/posts/{postId}`. */
    suspend fun deletePost(postId: Long): Result<Unit, DataError.Network>

    /** 게시글 좋아요 - `POST /community/posts/{postId}/like`. */
    suspend fun likePost(postId: Long): Result<Unit, DataError.Network>

    /** 게시글 좋아요 취소 - `DELETE /community/posts/{postId}/like`. */
    suspend fun unlikePost(postId: Long): Result<Unit, DataError.Network>

    /** 게시글 댓글 목록 - `GET /community/posts/{postId}/comments`. */
    suspend fun getComments(
        postId: Long,
        lastId: Long? = null,
        size: Int = 20,
    ): Result<CommunityCommentsResponse, DataError.Network>

    /** 댓글/대댓글 작성 - `POST /community/posts/{postId}/comments`. */
    suspend fun createComment(
        postId: Long,
        content: String,
        parentCommentId: Long? = null,
    ): Result<CreateCommentResponse, DataError.Network>

    /** 댓글 수정 - `PATCH /community/comments/{commentId}`. */
    suspend fun updateComment(commentId: Long, content: String): Result<Unit, DataError.Network>

    /** 댓글 삭제 - `DELETE /community/comments/{commentId}`. */
    suspend fun deleteComment(commentId: Long): Result<Unit, DataError.Network>

    /** 댓글 좋아요 - `POST /community/comments/{commentId}/like`. */
    suspend fun likeComment(commentId: Long): Result<Unit, DataError.Network>

    /** 댓글 좋아요 취소 - `DELETE /community/comments/{commentId}/like`. */
    suspend fun unlikeComment(commentId: Long): Result<Unit, DataError.Network>

    /** 신고 - `POST /community/reports`. */
    suspend fun report(
        targetType: ReportTargetType,
        targetId: Long,
        reportCategory: ReportCategory,
    ): Result<Unit, DataError.Network>
}
