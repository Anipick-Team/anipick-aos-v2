package com.jparkbro.ranking.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.common.CommonRepository
import com.jparkbro.core.data.ranking.RankingRepository
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.metadata.FilterType
import com.jparkbro.core.model.pagination.Cursor
import com.jparkbro.core.ui.GlobalSnackbarManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class RankingViewModel(
    private val rankingRepository: RankingRepository,
    private val commonRepository: CommonRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
) : ViewModel() {

    private val _state = MutableStateFlow(RankingState())
    val state: StateFlow<RankingState> = _state.asStateFlow()

    init {
        fetchMetadata()
        loadRankings(resetCursor = true)
    }

    fun onAction(action: RankingAction) {
        when (action) {
            is RankingAction.OnRankingTypeSelected -> {
                if (_state.value.rankingType == action.rankingType) return
                _state.update {
                    it.copy(
                        rankingType = action.rankingType,
                        year = null,
                        season = null,
                        activeFilterSheet = null,
                    )
                }
                loadRankings(resetCursor = true)
            }

            is RankingAction.OnFilterChipClick -> {
                _state.update { it.copy(activeFilterSheet = action.filterType) }
            }

            RankingAction.OnFilterSheetDismiss -> {
                _state.update { it.copy(activeFilterSheet = null) }
            }

            is RankingAction.OnAnimeFilterConfirm -> {
                val openedForYearSeason = _state.value.activeFilterSheet == FilterType.YEAR
                _state.update {
                    it.copy(
                        year = action.year,
                        season = action.season,
                        genre = action.genre,
                        rankingType = if (openedForYearSeason) RankingType.YEAR_SEASON else it.rankingType,
                        activeFilterSheet = null,
                    )
                }
                loadRankings(resetCursor = true)
            }

            RankingAction.OnLoadMore -> loadMore()
            RankingAction.OnRetryClick -> loadRankings(resetCursor = true)
            RankingAction.OnMetadataRetryClick -> fetchMetadata()

            RankingAction.OnSearchClick,
            is RankingAction.OnAnimeClick,
            -> Unit // 네비게이션만 필요한 액션은 Root에서 처리한다.
        }
    }

    private fun loadMore() {
        val current = _state.value
        if (current.isLoading || current.isLoadingMore || current.endReached) return
        if (current.cursor == null) return
        loadRankings(resetCursor = false)
    }

    private fun loadRankings(resetCursor: Boolean) {
        viewModelScope.launch {
            val current = _state.value
            _state.update {
                if (resetCursor) it.copy(isLoading = true, error = null) else it.copy(isLoadingMore = true)
            }

            val lastId = if (resetCursor) null else current.cursor?.lastId
            val lastValue = if (resetCursor) null else current.cursor?.lastValue
            val lastRank = if (resetCursor) null else current.animes.lastOrNull()?.rank?.toString()
            val genreName = current.genre?.name

            val result = when (current.rankingType) {
                RankingType.REAL_TIME -> rankingRepository.getRealTimeRankings(
                    genre = genreName,
                    lastId = lastId,
                    lastValue = lastValue,
                    size = PAGE_SIZE,
                )
                RankingType.YEAR_SEASON -> rankingRepository.getYearSeasonRankings(
                    year = current.year,
                    season = current.season?.id,
                    genre = genreName,
                    lastId = lastId,
                    lastRank = lastRank,
                    size = PAGE_SIZE,
                )
                RankingType.ALL_TIME -> rankingRepository.getAllTimeRankings(
                    genre = genreName,
                    lastId = lastId,
                    lastRank = lastRank,
                    size = PAGE_SIZE,
                )
            }

            result
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

    private fun handleMetadataFailure(error: DataError.Network) {
        when (error) {
            DataError.Network.NO_INTERNET -> globalSnackbarManager.showSnackbar(error.toDisplayMessage())
            else -> _state.update { it.copy(isMetadataError = true) }
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
