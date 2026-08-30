package com.jparkbro.core.data.community

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.model.community.CommunityBoard
import com.jparkbro.core.model.community.CommunityComment
import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.model.pagination.CursorPage
import com.jparkbro.core.model.report.ReportCategory
import com.jparkbro.core.model.report.ReportTargetType
import com.jparkbro.core.network.common.toCursor
import com.jparkbro.core.network.community.CommunityNetworkDataSource
import com.jparkbro.core.network.community.dto.CommunityExploreBoardsRequest
import com.jparkbro.core.network.community.dto.CommunityPostsRequest
import com.jparkbro.core.network.community.dto.toCommunityBoard
import com.jparkbro.core.network.community.dto.toCommunityComment
import com.jparkbro.core.network.community.dto.toCommunityPost
import com.jparkbro.core.network.image.ImageNetworkDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CommunityRepositoryImpl(
    private val communityNetworkDataSource: CommunityNetworkDataSource,
    private val imageNetworkDataSource: ImageNetworkDataSource,
) : CommunityRepository {

    private val _communityBoardPosts = MutableStateFlow(CommunityBoardPostsState())
    override val communityBoardPosts: StateFlow<CommunityBoardPostsState> = _communityBoardPosts.asStateFlow()

    override suspend fun loadCommunityBoardPosts(seriesId: Long, sort: String?, resetCursor: Boolean) {
        val current = _communityBoardPosts.value

        _communityBoardPosts.update {
            if (resetCursor) {
                it.copy(seriesId = seriesId, sort = sort, isLoading = true, error = null)
            } else {
                it.copy(isLoadingMore = true)
            }
        }

        val lastId = if (resetCursor) null else current.cursor?.lastId
        val lastValue = if (resetCursor) null else current.cursor?.lastValue

        getCommunityPosts(seriesId = seriesId, sort = sort, lastId = lastId, lastValue = lastValue, size = PAGE_SIZE)
            .onSuccess { page ->
                val items = page.items ?: emptyList()
                _communityBoardPosts.update {
                    it.copy(
                        seriesId = seriesId,
                        sort = sort,
                        posts = if (resetCursor) items else it.posts + items,
                        cursor = page.cursor,
                        endReached = items.size < PAGE_SIZE || page.cursor == null,
                        isLoading = false,
                        isLoadingMore = false,
                    )
                }
            }
            .onFailure { error ->
                _communityBoardPosts.update { it.copy(isLoading = false, isLoadingMore = false, error = error) }
            }
    }

    override suspend fun refreshCommunityBoardPosts() {
        val cache = _communityBoardPosts.value
        val seriesId = cache.seriesId ?: return
        loadCommunityBoardPosts(seriesId = seriesId, sort = cache.sort, resetCursor = true)
    }

    override fun clearCommunityBoardPosts() {
        _communityBoardPosts.value = CommunityBoardPostsState()
    }

    override suspend fun getCommunityBoardByAnime(animeId: Long): Result<CommunityBoard, DataError.Network> {
        return communityNetworkDataSource.getCommunityBoardByAnime(animeId).map { it.toCommunityBoard() }
    }

    override suspend fun getExploreCommunityBoards(
        sort: String?,
        keyword: String?,
        lastId: Long?,
        lastValue: String?,
        size: Int,
    ): Result<CursorPage<CommunityBoard>, DataError.Network> {
        val request = CommunityExploreBoardsRequest(
            sort = sort,
            keyword = keyword,
            lastId = lastId,
            lastValue = lastValue,
            size = size,
        )
        return communityNetworkDataSource.getExploreCommunityBoards(request).map { response ->
            CursorPage(
                cursor = response.cursor.toCursor(),
                items = response.boards?.map { it.toCommunityBoard() },
                count = response.count,
            )
        }
    }

    override suspend fun getCommunityPosts(
        seriesId: Long,
        sort: String?,
        lastId: Long?,
        lastValue: String?,
        size: Int,
    ): Result<CursorPage<CommunityPost>, DataError.Network> {
        val request = CommunityPostsRequest(
            sort = sort,
            lastId = lastId,
            lastValue = lastValue,
            size = size,
        )
        return communityNetworkDataSource.getCommunityPosts(seriesId, request).map { response ->
            CursorPage(
                cursor = response.cursor.toCursor(),
                items = response.posts?.map { it.toCommunityPost() },
            )
        }
    }

    override suspend fun uploadPostImage(
        imageBytes: ByteArray,
        fileName: String,
        mimeType: String,
    ): Result<Long, DataError.Network> {
        return imageNetworkDataSource.uploadCommunityPostImage(imageBytes, fileName, mimeType)
            .map { it.imageId }
    }

    override suspend fun createPost(
        seriesId: Long,
        title: String,
        content: String,
        isSpoiler: Boolean,
        imageIds: List<Long>,
    ): Result<Long, DataError.Network> {
        return communityNetworkDataSource.createPost(seriesId, title, content, isSpoiler, imageIds)
            .map { it.postId }
    }

    override suspend fun getPostDetail(postId: Long): Result<CommunityPost, DataError.Network> {
        return communityNetworkDataSource.getPostDetail(postId).map { it.toCommunityPost() }
    }

    override suspend fun updatePost(
        postId: Long,
        title: String,
        content: String,
        isSpoiler: Boolean,
        imageIds: List<Long>,
    ): Result<Unit, DataError.Network> {
        return communityNetworkDataSource.updatePost(postId, title, content, isSpoiler, imageIds)
    }

    override suspend fun deletePost(postId: Long): Result<Unit, DataError.Network> {
        return communityNetworkDataSource.deletePost(postId)
    }

    override suspend fun likePost(postId: Long): Result<Unit, DataError.Network> {
        return communityNetworkDataSource.likePost(postId)
    }

    override suspend fun unlikePost(postId: Long): Result<Unit, DataError.Network> {
        return communityNetworkDataSource.unlikePost(postId)
    }

    override suspend fun getComments(
        postId: Long,
        lastId: Long?,
        size: Int,
    ): Result<CursorPage<CommunityComment>, DataError.Network> {
        return communityNetworkDataSource.getComments(postId, lastId, size).map { response ->
            CursorPage(
                cursor = response.cursor.toCursor(),
                items = response.comments?.map { it.toCommunityComment() },
            )
        }
    }

    override suspend fun createComment(
        postId: Long,
        content: String,
        parentCommentId: Long?,
    ): Result<Long, DataError.Network> {
        return communityNetworkDataSource.createComment(postId, content, parentCommentId)
            .map { it.commentId }
    }

    override suspend fun updateComment(commentId: Long, content: String): Result<Unit, DataError.Network> {
        return communityNetworkDataSource.updateComment(commentId, content)
    }

    override suspend fun deleteComment(commentId: Long): Result<Unit, DataError.Network> {
        return communityNetworkDataSource.deleteComment(commentId)
    }

    override suspend fun likeComment(commentId: Long): Result<Unit, DataError.Network> {
        return communityNetworkDataSource.likeComment(commentId)
    }

    override suspend fun unlikeComment(commentId: Long): Result<Unit, DataError.Network> {
        return communityNetworkDataSource.unlikeComment(commentId)
    }

    override suspend fun report(
        targetType: ReportTargetType,
        targetId: Long,
        reportCategory: ReportCategory,
    ): Result<Unit, DataError.Network> {
        return communityNetworkDataSource.report(targetType, targetId, reportCategory)
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
