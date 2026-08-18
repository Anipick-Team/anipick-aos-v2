package com.jparkbro.core.network.home

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.home.dto.ComingSoonAnimeResponse
import com.jparkbro.core.network.home.dto.RecentReviewResponse
import com.jparkbro.core.network.home.dto.RecommendationAnimesResponse
import com.jparkbro.core.network.home.dto.TrendingAnimeResponse
import com.jparkbro.core.network.home.dto.WeeklyAnimeResponse

interface HomeNetworkDataSource {
    suspend fun getTrendingAnimes(): Result<List<TrendingAnimeResponse>, DataError.Network>
    suspend fun getWeeklyAnimes(day: String): Result<List<WeeklyAnimeResponse>, DataError.Network>
    suspend fun getRecommendationAnimes(): Result<RecommendationAnimesResponse, DataError.Network>
    suspend fun getRecentAnimeRecommendations(
        animeId: Long,
    ): Result<RecommendationAnimesResponse, DataError.Network>
    suspend fun getComingSoonAnimes(): Result<List<ComingSoonAnimeResponse>, DataError.Network>
    suspend fun getRecentReviews(): Result<List<RecentReviewResponse>, DataError.Network>
}
