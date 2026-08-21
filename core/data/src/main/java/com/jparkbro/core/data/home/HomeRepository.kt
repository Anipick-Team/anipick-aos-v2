package com.jparkbro.core.data.home

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.anime.RecommendationResult
import com.jparkbro.core.model.review.Review

/** 홈 화면 데이터를 읽어오는 인터페이스 */
interface HomeRepository {
    /** 홈 화면 인기 애니 - `GET /home/animes/trending`. */
    suspend fun getTrendingAnimes(): Result<List<Anime>, DataError.Network>
    /** 홈 화면 요일별 애니 - `GET /home/animes/weekly`. */
    suspend fun getWeeklyAnimes(day: String): Result<List<Anime>, DataError.Network>
    /** 홈 화면 추천 애니 - `GET /home/recommendation/animes`. */
    suspend fun getRecommendationAnimes(): Result<RecommendationResult, DataError.Network>
    /** 홈 화면 최근 조회 기반 추천 애니 - `GET /home/recommendation/animes/{animeId}/recent`. */
    suspend fun getRecentAnimeRecommendations(animeId: Long): Result<RecommendationResult, DataError.Network>
    /** 홈 화면 방영 예정 애니 - `GET /home/animes/coming-soon`. */
    suspend fun getComingSoonAnimes(): Result<List<Anime>, DataError.Network>
    /** 홈 화면 최근 리뷰 - `GET /home/reviews/recent`. */
    suspend fun getRecentReviews(): Result<List<Review>, DataError.Network>
}
