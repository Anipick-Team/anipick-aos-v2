package com.jparkbro.explore.impl

import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.common.CommonRepository
import com.jparkbro.core.data.community.CommunityRepository
import com.jparkbro.core.data.explore.ExploreRepository
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.community.CommunityBoard
import com.jparkbro.core.model.pagination.Cursor
import com.jparkbro.core.ui.GlobalSnackbarManager
import com.jparkbro.explore.api.ExploreEntryFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ExploreViewModel(
    private val exploreRepository: ExploreRepository,
    private val communityRepository: CommunityRepository,
    private val commonRepository: CommonRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
) : ViewModel() {

    private val _state = MutableStateFlow(ExploreState())
    val state: StateFlow<ExploreState> = _state.asStateFlow()

    init {
        val entryFilter = ExploreEntryFilter.consume()
        _state.update { it.copy(year = entryFilter?.year) }

        val initialSeasonId = entryFilter?.season
        if (initialSeasonId != null) {
            fetchMetadataAndApplyInitialSeason(initialSeasonId)
        } else {
            fetchMetadata()
            loadAnimes(resetCursor = true)
        }
        loadCommunityBoards(resetCursor = true)
    }

    fun onAction(action: ExploreAction) {
        when (action) {
            is ExploreAction.OnTabSelected -> {
                if (_state.value.tab == action.tab) return
                _state.update { it.copy(tab = action.tab) }
            }

            is ExploreAction.OnSortSelected -> {
                when (_state.value.tab) {
                    ExploreTab.ANIME -> {
                        if (_state.value.sort == action.sort) return
                        _state.update { it.copy(sort = action.sort) }
                        loadAnimes(resetCursor = true)
                    }
                    ExploreTab.COMMUNITY -> {
                        if (_state.value.communitySort == action.sort) return
                        _state.update { it.copy(communitySort = action.sort) }
                        loadCommunityBoards(resetCursor = true)
                    }
                }
            }

            is ExploreAction.OnFilterChipClick -> {
                _state.update { it.copy(activeFilterSheet = action.filterType) }
            }

            ExploreAction.OnFilterSheetDismiss -> {
                _state.update { it.copy(activeFilterSheet = null) }
            }

            is ExploreAction.OnFilterConfirm -> {
                _state.update {
                    it.copy(
                        year = action.year,
                        season = action.season,
                        genres = action.genres,
                        genreOp = action.genreOp ?: it.genreOp,
                        type = action.type,
                        activeFilterSheet = null,
                    )
                }
                loadAnimes(resetCursor = true)
            }

            ExploreAction.OnYearFilterRemove -> {
                _state.update { it.copy(year = null, season = null) }
                loadAnimes(resetCursor = true)
            }

            ExploreAction.OnSeasonFilterRemove -> {
                _state.update { it.copy(season = null) }
                loadAnimes(resetCursor = true)
            }

            is ExploreAction.OnGenreFilterRemove -> {
                _state.update { it.copy(genres = it.genres - action.genre) }
                loadAnimes(resetCursor = true)
            }

            ExploreAction.OnTypeFilterRemove -> {
                _state.update { it.copy(type = null) }
                loadAnimes(resetCursor = true)
            }

            ExploreAction.OnLoadMore -> loadMore()
            ExploreAction.OnRetryClick -> loadAnimes(resetCursor = true)
            ExploreAction.OnMetadataRetryClick -> fetchMetadata()

            ExploreAction.OnSearchClick -> Unit // 네비게이션만 필요한 액션은 Root에서 처리한다.
            is ExploreAction.OnAnimeClick -> Unit // 네비게이션만 필요한 액션은 Root에서 처리한다.
            is ExploreAction.OnCommunityBoardClick -> Unit // 네비게이션만 필요한 액션은 Root에서 처리한다.

            ExploreAction.OnCommunitySearchClick -> loadCommunityBoards(resetCursor = true)
            ExploreAction.OnCommunitySearchClearClick -> {
                _state.value.communitySearchState.clearText()
                loadCommunityBoards(resetCursor = true)
            }
            ExploreAction.OnCommunityLoadMore -> loadMoreCommunity()
            ExploreAction.OnCommunityRetryClick -> loadCommunityBoards(resetCursor = true)
        }
    }

    private fun loadMore() {
        val current = _state.value
        if (current.isLoading || current.isLoadingMore || current.endReached) return
        if (current.cursor == null) return
        loadAnimes(resetCursor = false)
    }

    /** [resetCursor]가 true면 첫 페이지(정렬/필터 변경 포함), false면 무한스크롤로 이어붙인다. */
    private fun loadAnimes(resetCursor: Boolean) {
        viewModelScope.launch {
            val current = _state.value
            _state.update {
                if (resetCursor) it.copy(isLoading = true, error = null) else it.copy(isLoadingMore = true)
            }

            val lastId = if (resetCursor) null else current.cursor?.lastId
            val lastValue = if (resetCursor) null else current.cursor?.lastValue

            exploreRepository.getExploreAnimes(
                year = current.year,
                season = current.season?.id,
                genres = current.genres.map { it.id.toLong() }.takeIf { it.isNotEmpty() },
                genreOp = current.genreOp,
                type = current.type,
                sort = current.sort.apiValue,
                lastId = lastId,
                lastValue = lastValue,
                size = PAGE_SIZE,
            )
                .onSuccess { page -> applyLoadedPage(page.items ?: emptyList(), page.cursor, append = !resetCursor) }
                .onFailure { error ->
                    val message = error.toDisplayMessage()
                    _state.update {
                        if (resetCursor) it.copy(isLoading = false, error = message)
                        else it.copy(isLoadingMore = false, error = message)
                    }
                }
        }
    }

    private fun applyLoadedPage(animes: List<Anime>, cursor: Cursor?, append: Boolean) {
        _state.update {
            it.copy(
                animes = if (append) it.animes + animes else animes,
                cursor = cursor,
                endReached = animes.size < PAGE_SIZE,
                isLoading = false,
                isLoadingMore = false,
            )
        }
    }

    private fun loadMoreCommunity() {
        val current = _state.value
        if (current.isCommunityLoading || current.isCommunityLoadingMore || current.communityEndReached) return
        if (current.communityCursor == null) return
        loadCommunityBoards(resetCursor = false)
    }

    /** [resetCursor]가 true면 첫 페이지(정렬/검색어 변경 포함), false면 무한스크롤로 이어붙인다. */
    private fun loadCommunityBoards(resetCursor: Boolean) {
        viewModelScope.launch {
            val current = _state.value
            _state.update {
                if (resetCursor) it.copy(isCommunityLoading = true, communityError = null) else it.copy(isCommunityLoadingMore = true)
            }

            val lastId = if (resetCursor) null else current.communityCursor?.lastId
            val lastValue = if (resetCursor) null else current.communityCursor?.lastValue
            val keyword = current.communitySearchState.text.toString().takeIf { it.isNotBlank() }
            // 커뮤니티 게시판 목록 API는 애니 탭과 달리 "popular"/"latest" 값을 쓴다.
            val sort = if (current.communitySort == ExploreSort.LATEST) "latest" else "popular"

            communityRepository.getExploreCommunityBoards(
                sort = sort,
                keyword = keyword,
                lastId = lastId,
                lastValue = lastValue,
                size = COMMUNITY_PAGE_SIZE,
            )
                .onSuccess { result ->
                    applyLoadedCommunityPage(result.boards ?: emptyList(), result.cursor, append = !resetCursor)
                }
                .onFailure { error ->
                    val message = error.toDisplayMessage()
                    _state.update {
                        if (resetCursor) it.copy(isCommunityLoading = false, communityError = message)
                        else it.copy(isCommunityLoadingMore = false, communityError = message)
                    }
                }
        }
    }

    private fun applyLoadedCommunityPage(boards: List<CommunityBoard>, cursor: Cursor?, append: Boolean) {
        _state.update {
            it.copy(
                communityBoards = if (append) it.communityBoards + boards else boards,
                communityCursor = cursor,
                communityEndReached = boards.size < COMMUNITY_PAGE_SIZE,
                isCommunityLoading = false,
                isCommunityLoadingMore = false,
            )
        }
    }

    private fun fetchMetadata() {
        viewModelScope.launch {
            _state.update { it.copy(isMetadataError = false) }

            commonRepository.getMetadata()
                .onSuccess { metadata -> _state.update { it.copy(metadata = metadata) } }
                .onFailure { error ->
                    Timber.e("메타데이터 조회 실패: $error")
                    handleMetadataFailure(error)
                }
        }
    }

    /** [ExploreEntryFilter]로 넘어온 분기(id)는 이름이 없어 메타데이터의 [Season] 목록에서 찾아 채운
     *  다음에야 검색을 시작할 수 있다 - 다른 화면에서 넘어온 진입 경로에서만 타는 분기. */
    private fun fetchMetadataAndApplyInitialSeason(seasonId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isMetadataError = false) }

            commonRepository.getMetadata()
                .onSuccess { metadata ->
                    val initialSeason = metadata.seasons?.find { it.id == seasonId }
                    _state.update { it.copy(metadata = metadata, season = initialSeason) }
                }
                .onFailure { error ->
                    Timber.e("메타데이터 조회 실패: $error")
                    handleMetadataFailure(error)
                }
            loadAnimes(resetCursor = true)
        }
    }

    private fun handleMetadataFailure(error: DataError.Network) {
        when (error) {
            DataError.Network.NO_INTERNET -> globalSnackbarManager.showSnackbar(error.toDisplayMessage())
            else -> _state.update { it.copy(isMetadataError = true) }
        }
    }

    companion object {
        private const val PAGE_SIZE = 18
        private const val COMMUNITY_PAGE_SIZE = 20
    }
}
