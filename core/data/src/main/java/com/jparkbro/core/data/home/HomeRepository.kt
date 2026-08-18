package com.jparkbro.core.data.home

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.anime.RecommendationResult
import com.jparkbro.core.model.review.Review

interface HomeRepository {
    suspend fun getTrendingAnimes(): Result<List<Anime>, DataError.Network>
    suspend fun getWeeklyAnimes(day: String): Result<List<Anime>, DataError.Network>
    suspend fun getRecommendationAnimes(): Result<RecommendationResult, DataError.Network>
    suspend fun getRecentAnimeRecommendations(animeId: Long): Result<RecommendationResult, DataError.Network>
    suspend fun getComingSoonAnimes(): Result<List<Anime>, DataError.Network>
    suspend fun getRecentReviews(): Result<List<Review>, DataError.Network>
}
