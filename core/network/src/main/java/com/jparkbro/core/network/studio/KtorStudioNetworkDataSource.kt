package com.jparkbro.core.network.studio

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.get
import com.jparkbro.core.network.studio.dto.StudioAnimesResponse
import io.ktor.client.HttpClient

class KtorStudioNetworkDataSource(
    private val httpClient: HttpClient,
) : StudioNetworkDataSource {

    override suspend fun getStudioAnimes(
        studioId: Long,
        lastId: Long?,
        lastValue: String?,
        size: Int,
    ): Result<StudioAnimesResponse, DataError.Network> {
        return httpClient.get(
            route = "/studios/$studioId/animes",
            queryParameters = mapOf(
                "lastId" to lastId,
                "lastValue" to lastValue,
                "size" to size,
            ),
        )
    }
}
