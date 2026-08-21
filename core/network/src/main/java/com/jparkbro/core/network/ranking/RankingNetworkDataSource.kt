package com.jparkbro.core.network.ranking

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.ranking.dto.RankingAnimesResponse

interface RankingNetworkDataSource {
    /** 랭킹 - 실시간 탭 - `GET /rankings/real-time`. */
    suspend fun getRealTimeRankings(
        genre: String? = null,
        lastId: Long? = null,
        lastValue: String? = null,
        size: Int = 20,
    ): Result<RankingAnimesResponse, DataError.Network>

    /** 랭킹 - 연도/시즌 탭 - `GET /rankings/year-season`. */
    suspend fun getYearSeasonRankings(
        year: Int? = null,
        season: Int? = null,
        genre: String? = null,
        lastId: Long? = null,
        lastRank: String? = null,
        size: Int = 20,
    ): Result<RankingAnimesResponse, DataError.Network>

    /** 랭킹 - 역대 탭 - `GET /rankings/all-time`. */
    suspend fun getAllTimeRankings(
        genre: String? = null,
        lastId: Long? = null,
        lastRank: String? = null,
        size: Int = 20,
    ): Result<RankingAnimesResponse, DataError.Network>
}
