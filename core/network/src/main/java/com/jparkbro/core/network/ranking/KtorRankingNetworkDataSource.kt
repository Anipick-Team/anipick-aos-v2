package com.jparkbro.core.network.ranking

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.get
import com.jparkbro.core.network.ranking.dto.RankingAnimesResponse
import io.ktor.client.HttpClient

class KtorRankingNetworkDataSource(
    private val httpClient: HttpClient,
) : RankingNetworkDataSource {

    override suspend fun getRealTimeRankings(
        genre: String?,
        lastId: Long?,
        lastValue: String?,
        size: Int,
    ): Result<RankingAnimesResponse, DataError.Network> {
        return httpClient.get(
            route = "/rankings/real-time",
            queryParameters = mapOf(
                "genre" to genre,
                "lastId" to lastId,
                "lastValue" to lastValue,
                "size" to size,
            ),
        )
    }

    override suspend fun getYearSeasonRankings(
        year: Int?,
        season: Int?,
        genre: String?,
        lastId: Long?,
        lastRank: String?,
        size: Int,
    ): Result<RankingAnimesResponse, DataError.Network> {
        return httpClient.get(
            route = "/rankings/year-season",
            queryParameters = mapOf(
                "year" to year,
                "season" to season,
                "genre" to genre,
                "lastId" to lastId,
                "lastRank" to lastRank,
                "size" to size,
            ),
        )
    }

    override suspend fun getAllTimeRankings(
        genre: String?,
        lastId: Long?,
        lastRank: String?,
        size: Int,
    ): Result<RankingAnimesResponse, DataError.Network> {
        return httpClient.get(
            route = "/rankings/all-time",
            queryParameters = mapOf(
                "genre" to genre,
                "lastId" to lastId,
                "lastRank" to lastRank,
                "size" to size,
            ),
        )
    }
}
