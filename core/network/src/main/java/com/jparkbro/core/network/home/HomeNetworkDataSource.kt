package com.jparkbro.core.network.home

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.home.dto.ComingSoonAnimeResponse
import com.jparkbro.core.network.home.dto.RecentReviewResponse
import com.jparkbro.core.network.home.dto.RecommendationAnimesResponse
import com.jparkbro.core.network.home.dto.TrendingAnimeResponse
import com.jparkbro.core.network.home.dto.WeeklyAnimeResponse

interface HomeNetworkDataSource {
    /** 홈 메인 - 인기 애니 목록 - `GET /home/animes/trending`. */
    suspend fun getTrendingAnimes(): Result<List<TrendingAnimeResponse>, DataError.Network>

    /** 홈 메인 - 요일별 애니 목록 - `GET /home/animes/weekly`. */
    suspend fun getWeeklyAnimes(day: String): Result<List<WeeklyAnimeResponse>, DataError.Network>

    /** 홈 메인 - 추천 애니 목록 - `GET /home/recommendation/animes`. */
    suspend fun getRecommendationAnimes(): Result<RecommendationAnimesResponse, DataError.Network>

    /** 홈 메인 - 최근 본 애니 기반 추천 - `GET /home/recommendation/animes/{animeId}/recent`. */
    suspend fun getRecentAnimeRecommendations(
        animeId: Long,
    ): Result<RecommendationAnimesResponse, DataError.Network>

    /** 홈 메인 - 방영예정작 미리보기 - `GET /home/animes/coming-soon`. */
    suspend fun getComingSoonAnimes(): Result<List<ComingSoonAnimeResponse>, DataError.Network>

    /** 홈 메인 - 최근 리뷰 목록 - `GET /home/reviews/recent`. */
    suspend fun getRecentReviews(): Result<List<RecentReviewResponse>, DataError.Network>
}
