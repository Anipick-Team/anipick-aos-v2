package com.jparkbro.core.network.anime

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.anime.dto.ComingSoonAnimesDetailResponse
import com.jparkbro.core.network.anime.dto.ComingSoonAnimesRequest
import com.jparkbro.core.network.anime.dto.PreferenceSetupSearchRequest
import com.jparkbro.core.network.anime.dto.PreferenceSetupSearchResponse
import com.jparkbro.core.network.anime.dto.UpcomingSeasonAnimesResponse
import com.jparkbro.core.network.get
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
}
