package com.jparkbro.core.data.anime

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.datastore.RecentAnimeDataStore
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.anime.PreferenceSetupSearchResult
import com.jparkbro.core.model.anime.RecommendationResult
import com.jparkbro.core.model.anime.UpcomingSeasonResult
import com.jparkbro.core.network.anime.AnimeNetworkDataSource
import com.jparkbro.core.network.anime.PreferenceSetupSearchRequest
import com.jparkbro.core.network.anime.toAnime
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

    override suspend fun getTrendingAnimes(): Result<List<Anime>, DataError.Network> {
        return animeNetworkDataSource.getTrendingAnimes().map { responses ->
            responses.map { it.toAnime() }
        }
    }

    override suspend fun getRecommendationAnimes(): Result<RecommendationResult, DataError.Network> {
        return animeNetworkDataSource.getRecommendationAnimes().map { response ->
            RecommendationResult(
                referenceAnimeTitle = response.referenceAnimeTitle,
                animes = response.animes.map { it.toAnime() },
            )
        }
    }

    override suspend fun getRecentAnimeRecommendations(animeId: Long): Result<RecommendationResult, DataError.Network> {
        return animeNetworkDataSource.getRecentAnimeRecommendations(animeId).map { response ->
            RecommendationResult(
                referenceAnimeTitle = response.referenceAnimeTitle,
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

    override suspend fun getComingSoonAnimes(): Result<List<Anime>, DataError.Network> {
        return animeNetworkDataSource.getComingSoonAnimes().map { responses ->
            responses.map { it.toAnime() }
        }
    }
}
