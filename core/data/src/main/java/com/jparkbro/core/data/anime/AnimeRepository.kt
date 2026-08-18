package com.jparkbro.core.data.anime

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.ComingSoonResult
import com.jparkbro.core.model.anime.PreferenceSetupSearchResult
import com.jparkbro.core.model.anime.UpcomingSeasonResult
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {

    val recentAnimeId: Flow<Long?>

    suspend fun searchPreferenceSetupAnimes(
        query: String? = null,
        year: String? = null,
        season: Int? = null,
        genres: Int? = null,
        lastId: Long? = null,
        size: Int? = 10,
    ): Result<PreferenceSetupSearchResult, DataError.Network>

    suspend fun getUpcomingSeasonAnimes(): Result<UpcomingSeasonResult, DataError.Network>

    suspend fun getComingSoonAnimesDetail(
        sort: String? = null,
        lastId: Long? = null,
        lastValue: String? = null,
        size: Long = 18,
    ): Result<ComingSoonResult, DataError.Network>
}
