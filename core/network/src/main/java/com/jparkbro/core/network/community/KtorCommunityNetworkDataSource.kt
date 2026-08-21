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
import com.jparkbro.core.network.community.dto.CreateCommentRequest
import com.jparkbro.core.network.community.dto.CreateCommentResponse
import com.jparkbro.core.network.community.dto.CreatePostRequest
import com.jparkbro.core.network.community.dto.CreatePostResponse
import com.jparkbro.core.network.community.dto.ReportRequest
import com.jparkbro.core.network.community.dto.UpdateCommentRequest
import com.jparkbro.core.network.community.dto.UpdatePostRequest
import com.jparkbro.core.network.delete
import com.jparkbro.core.network.get
import com.jparkbro.core.network.patch
import com.jparkbro.core.network.post
import io.ktor.client.HttpClient

class KtorCommunityNetworkDataSource(
    private val httpClient: HttpClient,
) : CommunityNetworkDataSource {

    override suspend fun getCommunityBoardByAnime(animeId: Long): Result<CommunityBoardResponse, DataError.Network> {
        return httpClient.get(route = "/community/boards/by-anime/$animeId")
    }

    override suspend fun getExploreCommunityBoards(
        request: CommunityExploreBoardsRequest,
    ): Result<CommunityExploreBoardsResponse, DataError.Network> {
        return httpClient.get(
            route = "/community/explore/boards",
            queryParameters = mapOf(
                "sort" to request.sort,
                "keyword" to request.keyword,
                "lastId" to request.lastId,
                "lastValue" to request.lastValue,
                "size" to request.size,
            ),
        )
    }

    override suspend fun getCommunityPosts(
        seriesId: Long,
        request: CommunityPostsRequest,
    ): Result<CommunityPostsResponse, DataError.Network> {
        return httpClient.get(
            route = "/community/boards/$seriesId/posts",
            queryParameters = mapOf(
                "sort" to request.sort,
                "lastId" to request.lastId,
                "lastValue" to request.lastValue,
                "size" to request.size,
            ),
        )
    }

    override suspend fun createPost(
        seriesId: Long,
        title: String,
        content: String,
        isSpoiler: Boolean,
        imageIds: List<Long>,
    ): Result<CreatePostResponse, DataError.Network> {
        return httpClient.post(
            route = "/community/posts",
            body = CreatePostRequest(
                seriesId = seriesId,
                title = title,
                content = content,
                isSpoiler = isSpoiler,
                imageIds = imageIds,
            ),
        )
    }

    override suspend fun getPostDetail(postId: Long): Result<CommunityPostDetailResponse, DataError.Network> {
        return httpClient.get(route = "/community/posts/$postId")
    }

    override suspend fun updatePost(
        postId: Long,
        title: String,
        content: String,
        isSpoiler: Boolean,
        imageIds: List<Long>,
    ): Result<Unit, DataError.Network> {
        return httpClient.patch(
            route = "/community/posts/$postId",
            body = UpdatePostRequest(
                title = title,
                content = content,
                isSpoiler = isSpoiler,
                imageIds = imageIds,
            ),
        )
    }

    override suspend fun deletePost(postId: Long): Result<Unit, DataError.Network> {
        return httpClient.delete(route = "/community/posts/$postId")
    }

    override suspend fun likePost(postId: Long): Result<Unit, DataError.Network> {
        return httpClient.post(route = "/community/posts/$postId/like")
    }

    override suspend fun unlikePost(postId: Long): Result<Unit, DataError.Network> {
        return httpClient.delete(route = "/community/posts/$postId/like")
    }

    override suspend fun getComments(
        postId: Long,
        lastId: Long?,
        size: Int,
    ): Result<CommunityCommentsResponse, DataError.Network> {
        return httpClient.get(
            route = "/community/posts/$postId/comments",
            queryParameters = mapOf(
                "lastId" to lastId,
                "size" to size,
            ),
        )
    }

    override suspend fun createComment(
        postId: Long,
        content: String,
        parentCommentId: Long?,
    ): Result<CreateCommentResponse, DataError.Network> {
        return httpClient.post(
            route = "/community/posts/$postId/comments",
            body = CreateCommentRequest(content = content, parentCommentId = parentCommentId),
        )
    }

    override suspend fun updateComment(commentId: Long, content: String): Result<Unit, DataError.Network> {
        return httpClient.patch(
            route = "/community/comments/$commentId",
            body = UpdateCommentRequest(content = content),
        )
    }

    override suspend fun deleteComment(commentId: Long): Result<Unit, DataError.Network> {
        return httpClient.delete(route = "/community/comments/$commentId")
    }

    override suspend fun likeComment(commentId: Long): Result<Unit, DataError.Network> {
        return httpClient.post(route = "/community/comments/$commentId/like")
    }

    override suspend fun unlikeComment(commentId: Long): Result<Unit, DataError.Network> {
        return httpClient.delete(route = "/community/comments/$commentId/like")
    }

    override suspend fun report(
        targetType: ReportTargetType,
        targetId: Long,
        reportCategory: ReportCategory,
    ): Result<Unit, DataError.Network> {
        return httpClient.post(
            route = "/community/reports",
            body = ReportRequest(
                targetType = targetType.name,
                targetId = targetId,
                reportCategory = reportCategory.name,
            ),
        )
    }
}
