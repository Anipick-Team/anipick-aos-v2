package com.jparkbro.core.network.explore

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.explore.dto.ExploreAnimesResponse
import com.jparkbro.core.network.get
import io.ktor.client.HttpClient

class KtorExploreNetworkDataSource(
    private val httpClient: HttpClient,
) : ExploreNetworkDataSource {

    override suspend fun getExploreAnimes(
        year: Int?,
        season: Int?,
        genres: List<Long>?,
        genreOp: String?,
        type: String?,
        sort: String?,
        lastId: Long?,
        lastValue: String?,
        size: Int,
    ): Result<ExploreAnimesResponse, DataError.Network> {
        return httpClient.get(
            route = "/explore/animes",
            queryParameters = mapOf(
                "year" to year,
                "season" to season,
                "genres" to genres?.joinToString(","),
                "genreOp" to genreOp,
                "type" to type,
                "sort" to sort,
                "lastId" to lastId,
                "lastValue" to lastValue,
                "size" to size,
            ),
        )
    }
}
