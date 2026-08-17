package com.jparkbro.core.network.anime

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.anime.dto.ComingSoonAnimeResponse
import com.jparkbro.core.network.anime.dto.ComingSoonAnimesDetailResponse
import com.jparkbro.core.network.anime.dto.ComingSoonAnimesRequest
import com.jparkbro.core.network.anime.dto.PreferenceSetupSearchRequest
import com.jparkbro.core.network.anime.dto.PreferenceSetupSearchResponse
import com.jparkbro.core.network.anime.dto.RecommendationAnimesDetailResponse
import com.jparkbro.core.network.anime.dto.RecommendationAnimesRequest
import com.jparkbro.core.network.anime.dto.RecommendationAnimesResponse
import com.jparkbro.core.network.anime.dto.TrendingAnimeResponse
import com.jparkbro.core.network.anime.dto.UpcomingSeasonAnimesResponse
import com.jparkbro.core.network.anime.dto.WeeklyAnimeResponse

interface AnimeNetworkDataSource {
    suspend fun searchPreferenceSetupAnimes(
        request: PreferenceSetupSearchRequest,
    ): Result<PreferenceSetupSearchResponse, DataError.Network>
    suspend fun getTrendingAnimes(): Result<List<TrendingAnimeResponse>, DataError.Network>
    suspend fun getWeeklyAnimes(day: String): Result<List<WeeklyAnimeResponse>, DataError.Network>
    suspend fun getRecommendationAnimes(): Result<RecommendationAnimesResponse, DataError.Network>
    suspend fun getRecentAnimeRecommendations(
        animeId: Long,
    ): Result<RecommendationAnimesResponse, DataError.Network>
    /** Home Detail "오늘의 추천작" 화면의 커서 페이지네이션 버전. `GET /recommendation/animes`. */
    suspend fun getRecommendationAnimesDetail(
        request: RecommendationAnimesRequest,
    ): Result<RecommendationAnimesDetailResponse, DataError.Network>
    /** Home Detail "최근 본 애니 기반 추천" 화면의 커서 페이지네이션 버전. `GET /recommendation/animes/{animeId}/recent`. */
    suspend fun getRecentAnimeRecommendationsDetail(
        animeId: Long,
        request: RecommendationAnimesRequest,
    ): Result<RecommendationAnimesDetailResponse, DataError.Network>
    suspend fun getUpcomingSeasonAnimes(): Result<UpcomingSeasonAnimesResponse, DataError.Network>
    suspend fun getComingSoonAnimes(): Result<List<ComingSoonAnimeResponse>, DataError.Network>
    /** Home Detail "공개예정" 화면의 커서 페이지네이션 버전. `GET /animes/coming-soon`. */
    suspend fun getComingSoonAnimesDetail(
        request: ComingSoonAnimesRequest,
    ): Result<ComingSoonAnimesDetailResponse, DataError.Network>
}
