package com.jparkbro.core.data.ranking

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.pagination.CursorPage

/** 랭킹 화면 데이터를 읽어오는 인터페이스 */
interface RankingRepository {
    /** 실시간 랭킹 - `GET /rankings/real-time`. */
    suspend fun getRealTimeRankings(
        genre: String? = null,
        lastId: Long? = null,
        lastValue: String? = null,
        size: Int = 20,
    ): Result<CursorPage<Anime>, DataError.Network>

    /** 연도/시즌별 랭킹 - `GET /rankings/year-season`. */
    suspend fun getYearSeasonRankings(
        year: Int? = null,
        season: Int? = null,
        genre: String? = null,
        lastId: Long? = null,
        lastRank: String? = null,
        size: Int = 20,
    ): Result<CursorPage<Anime>, DataError.Network>

    /** 역대 랭킹 - `GET /rankings/all-time`. */
    suspend fun getAllTimeRankings(
        genre: String? = null,
        lastId: Long? = null,
        lastRank: String? = null,
        size: Int = 20,
    ): Result<CursorPage<Anime>, DataError.Network>
}
