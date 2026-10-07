package com.jparkbro.home.impl.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.anime.AnimeRepository
import com.jparkbro.core.data.recommendation.RecommendationRepository
import com.jparkbro.core.data.user.UserRepository
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.pagination.Cursor
import com.jparkbro.core.model.pagination.CursorPage
import com.jparkbro.home.api.HomeDetailType
import com.jparkbro.home.impl.components.toDayCode
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeDetailViewModel(
    private val type: HomeDetailType,
    private val animeRepository: AnimeRepository,
    private val recommendationRepository: RecommendationRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeDetailState(type = type))
    val state: StateFlow<HomeDetailState> = _state.asStateFlow()

    private var weeklyJob: Job? = null

    init {
        when (type) {
            is HomeDetailType.Recommendation -> {
                loadRecommendation(type.basedOnAnimeId)
                if (type.basedOnAnimeId == null) observeNickname()
            }
            HomeDetailType.Weekly -> loadWeekly(_state.value.selectedDayOfWeek)
            HomeDetailType.ComingSoon -> loadComingSoon()
        }
    }

    private fun observeNickname() {
        viewModelScope.launch {
            userRepository.nickname.collect { nickname ->
                _state.update { it.copy(nickname = nickname) }
            }
        }
    }

    private fun loadRecommendation(basedOnAnimeId: Long?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = if (basedOnAnimeId != null) {
                recommendationRepository.getRecentAnimeRecommendationsDetail(animeId = basedOnAnimeId)
            } else {
                recommendationRepository.getRecommendationAnimesDetail()
            }

            result
                .onSuccess { recommendation ->
                    _state.update { it.copy(referenceAnimeTitle = recommendation.referenceAnimeTitle) }
                    applyLoadedPage(recommendation.animes ?: emptyList(), recommendation.cursor, append = false)
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toDisplayMessage()) }
                }
        }
    }

    private fun loadComingSoon() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            animeRepository.getComingSoonAnimesDetail(sort = _state.value.sort)
                .onSuccess { result -> applyLoadedPage(result.animes ?: emptyList(), result.cursor, append = false) }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toDisplayMessage()) }
                }
        }
    }

    /** [AniPickAnimeInfiniteGrid]나 리뷰 목록 스크롤의 onLoadMore가 부른다. 이미 로딩 중이거나
     *  마지막 페이지까지 다 불러왔으면(또는 아직 커서가 없으면) 아무것도 하지 않는다. */
    private fun loadMore() {
        val current = _state.value
        if (current.isLoading || current.isLoadingMore || current.endReached) return
        val cursor = current.cursor ?: return

        when (val currentType = current.type) {
            is HomeDetailType.Recommendation -> loadMoreRecommendation(currentType, cursor)
            HomeDetailType.ComingSoon -> loadMoreComingSoon(cursor)
            HomeDetailType.Weekly -> loadMoreWeekly(cursor)
        }
    }

    private fun loadMoreRecommendation(type: HomeDetailType.Recommendation, cursor: Cursor) {
        val basedOnAnimeId = type.basedOnAnimeId

        viewModelScope.launch {
            _state.update { it.copy(isLoadingMore = true) }

            val result = if (basedOnAnimeId != null) {
                recommendationRepository.getRecentAnimeRecommendationsDetail(
                    animeId = basedOnAnimeId,
                    lastId = cursor.lastId,
                    lastValue = cursor.lastValue,
                )
            } else {
                recommendationRepository.getRecommendationAnimesDetail(
                    lastId = cursor.lastId,
                    lastValue = cursor.lastValue,
                )
            }

            result
                .onSuccess { recommendation -> applyLoadedPage(recommendation.animes ?: emptyList(), recommendation.cursor, append = true) }
                .onFailure { error ->
                    _state.update { it.copy(isLoadingMore = false, error = error.toDisplayMessage()) }
                }
        }
    }

    private fun loadMoreComingSoon(cursor: Cursor) {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingMore = true) }

            animeRepository.getComingSoonAnimesDetail(
                sort = _state.value.sort,
                lastId = cursor.lastId,
                lastValue = cursor.lastValue,
            )
                .onSuccess { result -> applyLoadedPage(result.animes ?: emptyList(), result.cursor, append = true) }
                .onFailure { error ->
                    _state.update { it.copy(isLoadingMore = false, error = error.toDisplayMessage()) }
                }
        }
    }

    /** [append]가 false면 초기 로드(첫 페이지로 교체), true면 무한스크롤(뒤에 이어붙임). 두 경우 다
     *  [animes]가 [PAGE_SIZE]보다 적게 오거나 [cursor]가 null이면 마지막 페이지로 본다. */
    private fun applyLoadedPage(animes: List<Anime>, cursor: Cursor?, append: Boolean) {
        _state.update {
            it.copy(
                animes = if (append) it.animes + animes else animes,
                cursor = cursor,
                endReached = animes.size < PAGE_SIZE || cursor == null,
                isLoading = false,
                isLoadingMore = false,
            )
        }
    }

    fun onAction(action: HomeDetailAction) {
        when (action) {
            is HomeDetailAction.OnDaySelected -> {
                if (_state.value.selectedDayOfWeek == action.day) return
                _state.update { it.copy(selectedDayOfWeek = action.day) }
                loadWeekly(action.day)
            }
            is HomeDetailAction.OnSortSelected -> {
                if (_state.value.sort != action.sort) {
                    _state.update { it.copy(sort = action.sort) }
                    loadComingSoon()
                }
            }
            HomeDetailAction.OnLoadMore -> loadMore()
            HomeDetailAction.OnRetryClick -> retry()
            is HomeDetailAction.Navigation -> Unit // Root에서 처리한다.
        }
    }

    private fun retry() {
        when (val currentType = _state.value.type) {
            is HomeDetailType.Recommendation -> loadRecommendation(currentType.basedOnAnimeId)
            HomeDetailType.ComingSoon -> loadComingSoon()
            HomeDetailType.Weekly -> loadWeekly(_state.value.selectedDayOfWeek)
        }
    }

    private fun loadWeekly(day: String) {
        weeklyJob?.cancel()
        weeklyJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isLoadingMore = false, error = null) }

            animeRepository.getAnimesByDay(day = day.toDayCode(), size = PAGE_SIZE.toLong())
                .onSuccess { page -> applyLoadedPage(page.items ?: emptyList(), page.weeklyCursor(), append = false) }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, animes = emptyList(), error = error.toDisplayMessage()) }
                }
        }
    }

    private fun loadMoreWeekly(cursor: Cursor) {
        val day = _state.value.selectedDayOfWeek
        weeklyJob?.cancel()
        weeklyJob = viewModelScope.launch {
            _state.update { it.copy(isLoadingMore = true) }

            animeRepository.getAnimesByDay(day = day.toDayCode(), lastId = cursor.lastId, size = PAGE_SIZE.toLong())
                .onSuccess { page -> applyLoadedPage(page.items ?: emptyList(), page.weeklyCursor(), append = true) }
                .onFailure { error ->
                    _state.update { it.copy(isLoadingMore = false, error = error.toDisplayMessage()) }
                }
        }
    }

    /** 요일별 응답은 `lastId`가 null이면 마지막 페이지 - 커서 자체를 null로 바꿔 [applyLoadedPage]가 끝으로 보게 한다. */
    private fun CursorPage<Anime>.weeklyCursor(): Cursor? = cursor?.takeIf { it.lastId != null }

    companion object {
        private const val PAGE_SIZE = 18
    }
}
