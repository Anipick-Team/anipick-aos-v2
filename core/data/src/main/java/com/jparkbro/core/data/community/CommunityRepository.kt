package com.jparkbro.core.data.community

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.community.CommunityBoard
import com.jparkbro.core.model.community.CommunityBoardsResult
import com.jparkbro.core.model.community.CommunityComment
import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.model.pagination.CursorPage
import com.jparkbro.core.model.report.ReportCategory
import com.jparkbro.core.model.report.ReportTargetType

/** 커뮤니티 관련 데이터를 읽어오는 인터페이스 */
interface CommunityRepository {
    /** 애니 기준 커뮤니티 게시판 존재 여부 - `GET /community/boards/by-anime/{animeId}`. */
    suspend fun getCommunityBoardByAnime(animeId: Long): Result<CommunityBoard, DataError.Network>

    /** 커뮤니티 탐색 게시판 목록 - `GET /community/explore/boards`. */
    suspend fun getExploreCommunityBoards(
        sort: String? = null,
        keyword: String? = null,
        lastId: Long? = null,
        lastValue: String? = null,
        size: Int = 20,
    ): Result<CommunityBoardsResult, DataError.Network>

    /** 커뮤니티 게시판 게시글 목록 - `GET /community/boards/{seriesId}/posts`. */
    suspend fun getCommunityPosts(
        seriesId: Long,
        sort: String? = null,
        lastId: Long? = null,
        lastValue: String? = null,
        size: Int = 20,
    ): Result<CursorPage<CommunityPost>, DataError.Network>

    /** 커뮤니티 게시글 이미지 업로드(1장) - `POST /image/community-post-image`. 여러 장이면 이 메서드를
     *  반복 호출해 모두 성공한 뒤, 모아둔 imageId 목록으로 [createPost]를 호출한다. */
    suspend fun uploadPostImage(
        imageBytes: ByteArray,
        fileName: String,
        mimeType: String,
    ): Result<Long, DataError.Network>

    /** 게시글 등록 - `POST /community/posts`. */
    suspend fun createPost(
        seriesId: Long,
        title: String,
        content: String,
        isSpoiler: Boolean,
        imageIds: List<Long>,
    ): Result<Long, DataError.Network>

    /** 게시글 상세 - `GET /community/posts/{postId}`. */
    suspend fun getPostDetail(postId: Long): Result<CommunityPost, DataError.Network>

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
    ): Result<CursorPage<CommunityComment>, DataError.Network>

    /** 댓글/대댓글 작성 - `POST /community/posts/{postId}/comments`. */
    suspend fun createComment(
        postId: Long,
        content: String,
        parentCommentId: Long? = null,
    ): Result<Long, DataError.Network>

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
