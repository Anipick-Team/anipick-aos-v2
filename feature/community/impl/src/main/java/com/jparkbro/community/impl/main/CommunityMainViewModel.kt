package com.jparkbro.community.impl.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.community.CommunityRepository
import com.jparkbro.core.ui.GlobalSnackbarManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** 상단 애니 정보(title/coverImageUrl/genres)는 진입하는 쪽이 이미 조회해둔 [CommunityBoard]
 *  [com.jparkbro.core.model.community.CommunityBoard] 값을 그대로 받아서 쓴다 - 이 화면에서 다시
 *  조회하지 않는다. 게시글 목록은 [CommunityRepository.communityBoardPosts] 캐시를 구독한다 - 글
 *  등록/수정/삭제로 다른 화면(글쓰기/게시글 상세)이 캐시를 재조회시키면 여기 재진입 없이 같이 갱신된다.
 *  이 화면을 완전히 벗어나면([onCleared]) 캐시를 비워서, 다른 게시판에 새로 들어가면 빈 상태로 시작한다. */
class CommunityMainViewModel(
    private val seriesId: Long,
    title: String?,
    coverImageUrl: String?,
    genres: List<String>?,
    private val communityRepository: CommunityRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
) : ViewModel() {

    private val _state = MutableStateFlow(
        CommunityMainState(
            seriesId = seriesId,
            boardTitle = title,
            boardCoverImageUrl = coverImageUrl,
            boardGenres = genres ?: emptyList(),
        )
    )
    val state: StateFlow<CommunityMainState> = _state.asStateFlow()

    init {
        observeBoardPosts()
        loadPosts(resetCursor = true)
    }

    override fun onCleared() {
        communityRepository.clearCommunityBoardPosts()
    }

    fun onAction(action: CommunityMainAction) {
        when (action) {
            is CommunityMainAction.Navigation -> Unit // Root에서 처리한다.
            is CommunityMainAction.OnFilterClick -> onFilterClick(action.filter)
            is CommunityMainAction.OnSpoilerVisibleChange -> _state.update { it.copy(isSpoilerVisible = action.isVisible) }
            CommunityMainAction.OnLoadMorePosts -> loadMorePosts()
            CommunityMainAction.OnRetryClick -> loadPosts(resetCursor = true)
        }
    }

    /** 이미 목록이 있는 상태에서 재조회(필터 변경/더 불러오기)가 실패하면 기존 목록은 그대로 두고
     *  스낵바로만 알린다 - 첫 진입 실패(목록이 비어있을 때)는 지금처럼 빈 화면 + 재시도 버튼으로 처리한다. */
    private fun observeBoardPosts() {
        viewModelScope.launch {
            communityRepository.communityBoardPosts.collect { cache ->
                val error = cache.error
                if (error != null && cache.posts.isNotEmpty()) {
                    globalSnackbarManager.showSnackbar(error.toDisplayMessage())
                }
                _state.update {
                    it.copy(
                        posts = cache.posts,
                        postsCursor = cache.cursor,
                        postsEndReached = cache.endReached,
                        isPostsLoading = cache.isLoading,
                        isLoadingMorePosts = cache.isLoadingMore,
                        error = cache.error?.toDisplayMessage(),
                    )
                }
            }
        }
    }

    private fun onFilterClick(filter: CommunityPostFilter) {
        if (_state.value.postFilter == filter) return
        _state.update { it.copy(postFilter = filter) }
        loadPosts(resetCursor = true)
    }

    private fun loadMorePosts() {
        val cache = communityRepository.communityBoardPosts.value
        if (cache.isLoading || cache.isLoadingMore || cache.endReached) return
        loadPosts(resetCursor = false)
    }

    private fun loadPosts(resetCursor: Boolean) {
        viewModelScope.launch {
            communityRepository.loadCommunityBoardPosts(
                seriesId = seriesId,
                sort = _state.value.postFilter.sortParam,
                resetCursor = resetCursor,
            )
        }
    }
}
