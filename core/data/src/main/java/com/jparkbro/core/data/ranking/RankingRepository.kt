package com.jparkbro.core.data.ranking

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.pagination.CursorPage

interface RankingRepository {
    suspend fun getRealTimeRankings(
        genre: String? = null,
        lastId: Long? = null,
        lastValue: String? = null,
        size: Int = 20,
    ): Result<CursorPage<Anime>, DataError.Network>

    suspend fun getYearSeasonRankings(
        year: Int? = null,
        season: Int? = null,
        genre: String? = null,
        lastId: Long? = null,
        lastRank: String? = null,
        size: Int = 20,
    ): Result<CursorPage<Anime>, DataError.Network>

    suspend fun getAllTimeRankings(
        genre: String? = null,
        lastId: Long? = null,
        lastRank: String? = null,
        size: Int = 20,
    ): Result<CursorPage<Anime>, DataError.Network>
}
