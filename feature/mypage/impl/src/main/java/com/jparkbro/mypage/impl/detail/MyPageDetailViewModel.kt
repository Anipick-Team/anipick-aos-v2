package com.jparkbro.mypage.impl.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.user.UserRepository
import com.jparkbro.core.model.anime.AnimeWatchStatus
import com.jparkbro.mypage.api.MyPageDetailType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    }

    fun onAction(action: MyPageDetailAction) {
        when (action) {
            is MyPageDetailAction.OnMyContentTabSelected -> {
                if (_state.value.myContentTab == action.tab) return
                _state.update {
                    it.copy(
                        myContentTab = action.tab,
                        posts = emptyList(),
                        comments = emptyList(),
                        cursor = null,
                        endReached = false,
                    )
                }
                load(resetCursor = true)
            }

            MyPageDetailAction.OnLoadMore -> loadMore()
            MyPageDetailAction.OnRetryClick -> load(resetCursor = true)

            is MyPageDetailAction.Navigation -> Unit // Root에서 처리한다.
        }
    }

    private fun loadMore() {
        val current = _state.value
        if (current.isLoading || current.isLoadingMore || current.endReached) return
        if (current.cursor == null) return
        load(resetCursor = false)
    }

    private fun load(resetCursor: Boolean) {
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
                MyPageDetailType.MyContent -> when (_state.value.myContentTab) {
                    MyContentTab.POSTS -> loadMyPosts(lastId, resetCursor)
                    MyContentTab.COMMENTS -> loadMyComments(lastId, resetCursor)
                }
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

    private suspend fun loadMyPosts(lastId: Long?, resetCursor: Boolean) {
        userRepository.getMyCommunityPosts(lastId = lastId, size = CONTENT_PAGE_SIZE)
            .onSuccess { result ->
                _state.update {
                    it.copy(
                        posts = if (resetCursor) result.items ?: emptyList() else it.posts + (result.items ?: emptyList()),
                        totalCount = result.count ?: it.totalCount,
                        cursor = result.cursor,
                        endReached = (result.items?.size ?: 0) < CONTENT_PAGE_SIZE,
                        isLoading = false,
                        isLoadingMore = false,
                    )
                }
            }
            .onFailure { error -> applyFailure(error, resetCursor) }
    }

    private suspend fun loadMyComments(lastId: Long?, resetCursor: Boolean) {
        userRepository.getMyCommunityComments(lastId = lastId, size = CONTENT_PAGE_SIZE)
            .onSuccess { result ->
                _state.update {
                    it.copy(
                        comments = if (resetCursor) result.items ?: emptyList() else it.comments + (result.items ?: emptyList()),
                        totalCount = result.count ?: it.totalCount,
                        cursor = result.cursor,
                        endReached = (result.items?.size ?: 0) < CONTENT_PAGE_SIZE,
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
        private const val CONTENT_PAGE_SIZE = 20
    }
}
