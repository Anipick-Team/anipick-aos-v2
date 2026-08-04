package com.jparkbro.core.data.anime

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.anime.PreferenceSetupSearchResult

interface AnimeRepository {
    suspend fun getTrendingAnimes(): Result<List<Anime>, DataError.Network>
    suspend fun searchPreferenceSetupAnimes(
        query: String? = null,
        year: String? = null,
        season: Int? = null,
        genres: Int? = null,
        lastId: Long? = null,
        size: Int? = 10,
    ): Result<PreferenceSetupSearchResult, DataError.Network>
}
