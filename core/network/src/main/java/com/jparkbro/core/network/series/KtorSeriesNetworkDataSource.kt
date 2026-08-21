package com.jparkbro.core.network.series

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.get
import com.jparkbro.core.network.series.dto.SeriesAnimesResponse
import io.ktor.client.HttpClient

class KtorSeriesNetworkDataSource(
    private val httpClient: HttpClient,
) : SeriesNetworkDataSource {

    override suspend fun getAnimeSeries(
        animeId: Long,
        lastId: Long?,
        size: Int,
    ): Result<SeriesAnimesResponse, DataError.Network> {
        return httpClient.get(
            route = "/animes/$animeId/series",
            queryParameters = mapOf(
                "lastId" to lastId,
                "size" to size,
            ),
        )
    }
}
