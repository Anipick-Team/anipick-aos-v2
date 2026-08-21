package com.jparkbro.core.network.anime

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.anime.dto.AnimeDetailResponse
import com.jparkbro.core.network.anime.dto.AnimeSummaryResponse
import com.jparkbro.core.network.anime.dto.ComingSoonAnimesDetailResponse
import com.jparkbro.core.network.anime.dto.ComingSoonAnimesRequest
import com.jparkbro.core.network.anime.dto.PreferenceSetupSearchRequest
import com.jparkbro.core.network.anime.dto.PreferenceSetupSearchResponse
import com.jparkbro.core.network.anime.dto.UpcomingSeasonAnimesResponse
import com.jparkbro.core.network.character.dto.AnimeCharacterResponse
import com.jparkbro.core.network.delete
import com.jparkbro.core.network.get
import com.jparkbro.core.network.post
import com.jparkbro.core.network.series.dto.SeriesAnimeResponse
import io.ktor.client.HttpClient

class KtorAnimeNetworkDataSource(
    private val httpClient: HttpClient,
) : AnimeNetworkDataSource {

    override suspend fun searchPreferenceSetupAnimes(
        request: PreferenceSetupSearchRequest,
    ): Result<PreferenceSetupSearchResponse, DataError.Network> {
        return httpClient.get(
            route = "/explore-search",
            queryParameters = mapOf(
                "query" to request.query,
                "year" to request.year,
                "season" to request.season,
                "genres" to request.genres,
                "lastId" to request.lastId,
                "size" to request.size,
            )
        )
    }

    override suspend fun getUpcomingSeasonAnimes(): Result<UpcomingSeasonAnimesResponse, DataError.Network> {
        return httpClient.get(
            route = "/animes/upcoming-season"
        )
    }

    override suspend fun getComingSoonAnimesDetail(
        request: ComingSoonAnimesRequest,
    ): Result<ComingSoonAnimesDetailResponse, DataError.Network> {
        return httpClient.get(
            route = "/animes/coming-soon",
            queryParameters = mapOf(
                "sort" to request.sort,
                "lastId" to request.lastId,
                "lastValue" to request.lastValue,
                "size" to request.size,
            ),
        )
    }

    override suspend fun getAnimeDetailInfo(animeId: Long): Result<AnimeDetailResponse, DataError.Network> {
        return httpClient.get(route = "/animes/$animeId/detail/info")
    }

    override suspend fun getAnimeDetailCast(animeId: Long): Result<List<AnimeCharacterResponse>, DataError.Network> {
        return httpClient.get(route = "/animes/$animeId/detail/actor")
    }

    override suspend fun getAnimeDetailSeries(animeId: Long): Result<List<SeriesAnimeResponse>, DataError.Network> {
        return httpClient.get(route = "/animes/$animeId/detail/series")
    }

    override suspend fun getAnimeDetailRecommendations(
        animeId: Long,
    ): Result<List<AnimeSummaryResponse>, DataError.Network> {
        return httpClient.get(route = "/animes/$animeId/detail/recommendation")
    }

    override suspend fun likeAnime(animeId: Long): Result<Unit, DataError.Network> {
        return httpClient.post(route = "/animes/$animeId/like")
    }

    override suspend fun unlikeAnime(animeId: Long): Result<Unit, DataError.Network> {
        return httpClient.delete(route = "/animes/$animeId/like")
    }
}
