package com.jparkbro.mypage.impl.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.user.MyCommunityCommentsState
import com.jparkbro.core.data.user.MyCommunityPostsState
import com.jparkbro.core.data.user.UserRepository
import com.jparkbro.core.model.anime.AnimeWatchStatus
import com.jparkbro.mypage.api.MyPageDetailType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyPageDetailViewModel(
    private val type: MyPageDetailType,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MyPageDetailState(type = type))
    val state: StateFlow<MyPageDetailState> = _state.asStateFlow()

    init {
        load(resetCursor = true)
        observeMyPageProfileChanges()
        observeMyContentCache()
    }

    /** [UserRepository.myPageProfile] 변경 시 1페이지부터 재조회 - MyContent는 구독 제외 */
    private fun observeMyPageProfileChanges() {
        if (type == MyPageDetailType.MyContent) return

        userRepository.myPageProfile
            .drop(1)
            .onEach { load(resetCursor = true) }
            .launchIn(viewModelScope)
    }

    /** [UserRepository.myCommunityPosts]/[myCommunityComments] 구독 - 게시글/댓글 작성·수정·삭제 시 자동 반영 */
    private fun observeMyContentCache() {
        if (type != MyPageDetailType.MyContent) return

        userRepository.myCommunityPosts
            .onEach { cache -> applyMyContentCache(MyContentTab.POSTS, posts = cache) }
            .launchIn(viewModelScope)

        userRepository.myCommunityComments
            .onEach { cache -> applyMyContentCache(MyContentTab.COMMENTS, comments = cache) }
            .launchIn(viewModelScope)
    }

    private fun applyMyContentCache(
        tab: MyContentTab,
        posts: MyCommunityPostsState? = null,
        comments: MyCommunityCommentsState? = null,
    ) {
        _state.update { current ->
            val isActiveTab = current.myContentTab == tab
            current.copy(
                posts = posts?.posts ?: current.posts,
                comments = comments?.comments ?: current.comments,
                isLoading = if (isActiveTab) (posts?.isLoading ?: comments?.isLoading ?: current.isLoading) else current.isLoading,
                isLoadingMore = if (isActiveTab) {
                    posts?.isLoadingMore ?: comments?.isLoadingMore ?: current.isLoadingMore
                } else {
                    current.isLoadingMore
                },
                cursor = if (isActiveTab) (posts?.cursor ?: comments?.cursor ?: current.cursor) else current.cursor,
                endReached = if (isActiveTab) (posts?.endReached ?: comments?.endReached ?: current.endReached) else current.endReached,
                totalCount = if (isActiveTab) (posts?.totalCount ?: comments?.totalCount ?: current.totalCount) else current.totalCount,
                error = if (isActiveTab) (posts?.error ?: comments?.error)?.toDisplayMessage() else current.error,
            )
        }
    }

    fun onAction(action: MyPageDetailAction) {
        when (action) {
            is MyPageDetailAction.OnMyContentTabSelected -> {
                if (_state.value.myContentTab == action.tab) return
                _state.update { it.copy(myContentTab = action.tab) }
                syncActiveMyContentTab()
                load(resetCursor = true)
            }

            MyPageDetailAction.OnLoadMore -> loadMore()
            MyPageDetailAction.OnRetryClick -> load(resetCursor = true)

            is MyPageDetailAction.Navigation -> Unit // Root에서 처리한다.
        }
    }

    /** 탭 전환 직후, 전환된 탭의 캐시가 이미 최신이라 재구독 이벤트가 안 오는 경우를 대비해 즉시 한 번 동기화 */
    private fun syncActiveMyContentTab() {
        when (_state.value.myContentTab) {
            MyContentTab.POSTS -> applyMyContentCache(MyContentTab.POSTS, posts = userRepository.myCommunityPosts.value)
            MyContentTab.COMMENTS -> applyMyContentCache(MyContentTab.COMMENTS, comments = userRepository.myCommunityComments.value)
        }
    }

    private fun loadMore() {
        val current = _state.value
        if (current.isLoading || current.isLoadingMore || current.endReached) return
        if (current.cursor == null) return
        load(resetCursor = false)
    }

    private fun load(resetCursor: Boolean) {
        if (type == MyPageDetailType.MyContent) {
            viewModelScope.launch {
                when (_state.value.myContentTab) {
                    MyContentTab.POSTS -> userRepository.loadMyCommunityPosts(resetCursor)
                    MyContentTab.COMMENTS -> userRepository.loadMyCommunityComments(resetCursor)
                }
            }
            return
        }

        viewModelScope.launch {
            _state.update {
                if (resetCursor) it.copy(isLoading = true, error = null) else it.copy(isLoadingMore = true)
            }

            val lastId = if (resetCursor) null else _state.value.cursor?.lastId

            when (type) {
                MyPageDetailType.WatchList -> loadAnimesByStatus(AnimeWatchStatus.WATCHLIST, lastId, resetCursor)
                MyPageDetailType.Watching -> loadAnimesByStatus(AnimeWatchStatus.WATCHING, lastId, resetCursor)
                MyPageDetailType.Finished -> loadAnimesByStatus(AnimeWatchStatus.FINISHED, lastId, resetCursor)
                MyPageDetailType.LikedAnimes -> loadLikedAnimes(lastId, resetCursor)
                MyPageDetailType.LikedPersons -> loadLikedPersons(lastId, resetCursor)
                MyPageDetailType.MyContent -> Unit
            }
        }
    }

    private suspend fun loadAnimesByStatus(status: AnimeWatchStatus, lastId: Long?, resetCursor: Boolean) {
        userRepository.getMyPageAnimes(status = status, lastId = lastId, size = PAGE_SIZE)
            .onSuccess { result ->
                _state.update {
                    it.copy(
                        animes = if (resetCursor) result.items ?: emptyList() else it.animes + (result.items ?: emptyList()),
                        totalCount = result.count ?: it.totalCount,
                        cursor = result.cursor,
                        endReached = (result.items?.size ?: 0) < PAGE_SIZE,
                        isLoading = false,
                        isLoadingMore = false,
                    )
                }
            }
            .onFailure { error -> applyFailure(error, resetCursor) }
    }

    private suspend fun loadLikedAnimes(lastId: Long?, resetCursor: Boolean) {
        userRepository.getLikedAnimes(lastId = lastId, size = PAGE_SIZE)
            .onSuccess { result ->
                _state.update {
                    it.copy(
                        animes = if (resetCursor) result.items ?: emptyList() else it.animes + (result.items ?: emptyList()),
                        totalCount = result.count ?: it.totalCount,
                        cursor = result.cursor,
                        endReached = (result.items?.size ?: 0) < PAGE_SIZE,
                        isLoading = false,
                        isLoadingMore = false,
                    )
                }
            }
            .onFailure { error -> applyFailure(error, resetCursor) }
    }

    private suspend fun loadLikedPersons(lastId: Long?, resetCursor: Boolean) {
        userRepository.getLikedPersons(lastId = lastId, size = PAGE_SIZE)
            .onSuccess { result ->
                _state.update {
                    it.copy(
                        persons = if (resetCursor) result.items ?: emptyList() else it.persons + (result.items ?: emptyList()),
                        totalCount = result.count ?: it.totalCount,
                        cursor = result.cursor,
                        endReached = (result.items?.size ?: 0) < PAGE_SIZE,
                        isLoading = false,
                        isLoadingMore = false,
                    )
                }
            }
            .onFailure { error -> applyFailure(error, resetCursor) }
    }

    private fun applyFailure(error: DataError.Network, resetCursor: Boolean) {
        val message = error.toDisplayMessage()
        _state.update {
            if (resetCursor) it.copy(isLoading = false, error = message) else it.copy(isLoadingMore = false, error = message)
        }
    }

    companion object {
        private const val PAGE_SIZE = 18
    }
}
