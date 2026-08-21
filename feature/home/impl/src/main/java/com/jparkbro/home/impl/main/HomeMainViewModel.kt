package com.jparkbro.home.impl.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.auth.TokenProvider
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.data.anime.AnimeRepository
import com.jparkbro.core.data.home.HomeRepository
import com.jparkbro.core.data.user.UserRepository
import com.jparkbro.core.model.anime.RecommendationResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeMainViewModel(
    private val animeRepository: AnimeRepository,
    private val homeRepository: HomeRepository,
    private val userRepository: UserRepository,
    private val tokenProvider: TokenProvider,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeMainState())
    val state: StateFlow<HomeMainState> = _state.asStateFlow()

    private var lastRefreshedAt = 0L

    init {
        observeNickname()
        observeRecentAnimeRecommendations()
        observeLoginState()
    }

    /** 로그인 상태가 false -> true로 바뀌면 디바운스 무시하고 강제 재조회 */
    private fun observeLoginState() {
        viewModelScope.launch {
            tokenProvider.isLoggedIn
                .drop(1) // 이 ViewModel이 생성된 시점의 최초 상태(이미 로그인돼 있음)는 무시
                .distinctUntilChanged()
                .filter { isLoggedIn -> isLoggedIn }
                .collect {
                    lastRefreshedAt = 0L
                    refresh()
                }
        }
    }

    fun onAction(action: HomeMainAction) {
        when (action) {
            is HomeMainAction.OnDaySelected -> selectDay(action.day)
            HomeMainAction.OnRetryClick -> refresh(force = true)
            else -> Unit // 네비게이션만 필요한 액션들은 Root에서 직접 처리한다.
        }
    }

    private fun selectDay(day: String) {
        _state.update { it.copy(selectedDayOfWeek = day) }
        viewModelScope.launch { loadWeeklyAnimes(day) }
    }

    /** 6개 섹션 동시 조회, 가장 늦게 끝나는 것까지 기다렸다가 [HomeMainState.isLoading] 해제.
     *  [force]가 true면 디바운스([REFRESH_INTERVAL_MS])를 무시한다 - 재시도 버튼은 방금 실패한
     *  직후에 눌리는 게 보통이라 디바운스에 걸리면 아무 반응이 없는 것처럼 보인다. */
    fun refresh(force: Boolean = false) {
        val now = System.currentTimeMillis()
        if (!force && now - lastRefreshedAt < REFRESH_INTERVAL_MS) {
            return
        }
        lastRefreshedAt = now

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isConnectionError = false) }

            val results = awaitAll(
                async { loadTrendingAnimes() },
                async { loadWeeklyAnimes(_state.value.selectedDayOfWeek) },
                async { loadRecommendationAnimes() },
                async { loadUpcomingSeasonAnimes() },
                async { loadComingSoonAnimes() },
                async { loadRecentReviews() },
            )

            // 전부 실패면(=대부분 네트워크 연결 자체 문제) 화면 전체를 재시도 유도로 바꾸고,
            // 일부만 실패면 그 섹션만 비어 보이도록 두고(기존 섹션별 가드) 나머지는 그대로 보여준다.
            _state.update { it.copy(isLoading = false, isConnectionError = results.all { success -> !success }) }
        }
    }

    private fun observeNickname() {
        viewModelScope.launch {
            userRepository.nickname.collect { nickname ->
                _state.update { it.copy(nickname = nickname) }
            }
        }
    }

    private suspend fun loadTrendingAnimes(): Boolean {
        val result = homeRepository.getTrendingAnimes()
        result.onSuccess { animes -> _state.update { it.copy(trendingAnimes = animes) } }
        return result is Result.Success
    }

    // TODO: 요일별 신작 API 미완성(백엔드 준비 안 됨, 지금 연동해도 정상 응답 아님) - 완성되면 주석 풀고 연동.
    // 그 전까진 weeklyAnimes를 빈 목록으로 둬서 "요일별 신작" 섹션 자체가 안 보이게 한다
    // (HomeMainSections.homeMainSections의 `if (state.weeklyAnimes.isNotEmpty())` 가드).
    // 아무 통신도 안 하니 refresh()의 "전부 실패했는지" 판정에서는 항상 성공(true) 취급한다.
    private suspend fun loadWeeklyAnimes(day: String): Boolean {
        // homeRepository.getWeeklyAnimes(day)
        //     .onSuccess { animes -> _state.update { it.copy(weeklyAnimes = animes) } }
        return true
    }

    private suspend fun loadRecommendationAnimes(): Boolean {
        val result = homeRepository.getRecommendationAnimes()
        result.onSuccess { recommendation -> _state.update { it.copy(recommendation = recommendation) } }
        return result is Result.Success
    }

    /** animeRepository.recentAnimeId를 구독해서, 값이 생기거나 바뀔 때마다 그 애니 기준 추천을
     *  다시 불러온다. 일반 추천([loadRecommendationAnimes])과는 둘 다 호출되는 별개의 섹션이다. */
    private fun observeRecentAnimeRecommendations() {
        viewModelScope.launch {
            animeRepository.recentAnimeId.collect { animeId ->
                if (animeId == null) {
                    _state.update {
                        it.copy(
                            recentAnimeRecommendationAnimeId = null,
                            recentAnimeRecommendation = RecommendationResult(),
                        )
                    }
                    return@collect
                }

                homeRepository.getRecentAnimeRecommendations(animeId)
                    .onSuccess { result ->
                        _state.update {
                            it.copy(
                                recentAnimeRecommendationAnimeId = animeId,
                                recentAnimeRecommendation = result,
                            )
                        }
                    }
            }
        }
    }

    private suspend fun loadUpcomingSeasonAnimes(): Boolean {
        val result = animeRepository.getUpcomingSeasonAnimes()
        result.onSuccess { upcomingSeason -> _state.update { it.copy(upcomingSeason = upcomingSeason) } }
        return result is Result.Success
    }

    private suspend fun loadRecentReviews(): Boolean {
        val result = homeRepository.getRecentReviews()
        result.onSuccess { reviews -> _state.update { it.copy(recentReviews = reviews) } }
        return result is Result.Success
    }

    private suspend fun loadComingSoonAnimes(): Boolean {
        val result = homeRepository.getComingSoonAnimes()
        result.onSuccess { animes -> _state.update { it.copy(comingSoonAnimes = animes) } }
        return result is Result.Success
    }

    companion object {
        private const val REFRESH_INTERVAL_MS = 300_000L
    }
}
