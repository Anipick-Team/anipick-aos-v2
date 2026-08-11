package com.jparkbro.core.network.anime

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result

interface AnimeNetworkDataSource {
    suspend fun searchPreferenceSetupAnimes(
        request: PreferenceSetupSearchRequest,
    ): Result<PreferenceSetupSearchResponse, DataError.Network>
    suspend fun getTrendingAnimes(): Result<List<TrendingAnimeResponse>, DataError.Network>
    suspend fun getRecommendationAnimes(): Result<RecommendationAnimesResponse, DataError.Network>
    suspend fun getRecentAnimeRecommendations(
        animeId: Long,
    ): Result<RecommendationAnimesResponse, DataError.Network>
    suspend fun getUpcomingSeasonAnimes(): Result<UpcomingSeasonAnimesResponse, DataError.Network>
    suspend fun getComingSoonAnimes(): Result<List<ComingSoonAnimeResponse>, DataError.Network>
}
