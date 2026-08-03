package com.jparkbro.core.network.anime

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result

interface AnimeNetworkDataSource {
    suspend fun getTrendingAnimes(): Result<List<TrendingAnimeResponse>, DataError.Network>
    suspend fun searchPreferenceSetupAnimes(
        request: PreferenceSetupSearchRequest,
    ): Result<PreferenceSetupSearchResponse, DataError.Network>
}
