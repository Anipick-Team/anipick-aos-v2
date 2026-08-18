package com.jparkbro.core.data.anime

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.datastore.RecentAnimeDataStore
import com.jparkbro.core.model.anime.ComingSoonResult
import com.jparkbro.core.model.anime.PreferenceSetupSearchResult
import com.jparkbro.core.model.anime.UpcomingSeasonResult
import com.jparkbro.core.network.anime.AnimeNetworkDataSource
import com.jparkbro.core.network.anime.dto.ComingSoonAnimesRequest
import com.jparkbro.core.network.anime.dto.PreferenceSetupSearchRequest
import com.jparkbro.core.network.anime.dto.toAnime
import com.jparkbro.core.network.common.toCursor
import kotlinx.coroutines.flow.Flow

class AnimeRepositoryImpl(
    private val animeNetworkDataSource: AnimeNetworkDataSource,
    recentAnimeDataStore: RecentAnimeDataStore,
) : AnimeRepository {

    override val recentAnimeId: Flow<Long?> = recentAnimeDataStore.recentAnimeId

    override suspend fun searchPreferenceSetupAnimes(
        query: String?,
        year: String?,
        season: Int?,
        genres: Int?,
        lastId: Long?,
        size: Int?,
    ): Result<PreferenceSetupSearchResult, DataError.Network> {
        val request = PreferenceSetupSearchRequest(
            query = query,
            year = year,
            season = season,
            genres = genres,
            lastId = lastId,
            size = size,
        )
        return animeNetworkDataSource.searchPreferenceSetupAnimes(request).map { response ->
            PreferenceSetupSearchResult(
                count = response.count,
                cursor = response.cursor.toCursor(),
                animes = response.animes.map { it.toAnime() },
            )
        }
    }

    override suspend fun getUpcomingSeasonAnimes(): Result<UpcomingSeasonResult, DataError.Network> {
        return animeNetworkDataSource.getUpcomingSeasonAnimes().map { response ->
            UpcomingSeasonResult(
                season = response.season,
                seasonYear = response.seasonYear,
                animes = response.animes.map { it.toAnime() },
            )
        }
    }

    override suspend fun getComingSoonAnimesDetail(
        sort: String?,
        lastId: Long?,
        lastValue: String?,
        size: Long,
    ): Result<ComingSoonResult, DataError.Network> {
        val request = ComingSoonAnimesRequest(sort = sort, lastId = lastId, lastValue = lastValue, size = size)
        return animeNetworkDataSource.getComingSoonAnimesDetail(request).map { response ->
            ComingSoonResult(
                count = response.count,
                cursor = response.cursor.toCursor(),
                animes = response.animes.map { it.toAnime() },
            )
        }
    }
}
