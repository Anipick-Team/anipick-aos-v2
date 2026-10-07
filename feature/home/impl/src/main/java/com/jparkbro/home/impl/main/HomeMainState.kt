package com.jparkbro.home.impl.main

import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.anime.RecommendationResult
import com.jparkbro.core.model.anime.UpcomingSeasonResult
import com.jparkbro.core.model.review.Review
import com.jparkbro.home.impl.components.todayDayOfWeek

data class HomeMainState(
    val nickname: String? = null,
    val trendingAnimes: List<Anime> = emptyList(),
    val recommendation: RecommendationResult = RecommendationResult(),
    val selectedDayOfWeek: String = todayDayOfWeek(),
    val weeklyAnimes: List<Anime> = emptyList(),
    /** 요일별 신작만 따로 조회하는 중인지 - 요일을 바꿀 때 목록 자리에 스켈레톤을 보여준다. */
    val isWeeklyLoading: Boolean = false,
    val recentReviews: List<Review> = emptyList(),
    val recentAnimeRecommendationAnimeId: Long? = null,
    val recentAnimeRecommendation: RecommendationResult = RecommendationResult(),
    val upcomingSeason: UpcomingSeasonResult = UpcomingSeasonResult(),
    val comingSoonAnimes: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    /** [HomeMainViewModel.refresh]에서 조회한 6개 섹션이 전부 실패했을 때만 true - 네트워크 연결
     *  자체가 끊긴 상황을 가리킨다. 일부만 실패한 경우엔 그 섹션만 조용히 안 보이고 이 값은 그대로 false. */
    val isConnectionError: Boolean = false,
)
