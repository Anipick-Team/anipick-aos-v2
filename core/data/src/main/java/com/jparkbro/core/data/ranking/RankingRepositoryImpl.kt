package com.jparkbro.core.data.ranking

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.pagination.CursorPage
import com.jparkbro.core.network.common.toCursor
import com.jparkbro.core.network.ranking.RankingNetworkDataSource
import com.jparkbro.core.network.ranking.dto.toAnime

class RankingRepositoryImpl(
    private val rankingNetworkDataSource: RankingNetworkDataSource,
) : RankingRepository {

    override suspend fun getRealTimeRankings(
        genre: String?,
        lastId: Long?,
        lastValue: String?,
        size: Int,
    ): Result<CursorPage<Anime>, DataError.Network> {
        return rankingNetworkDataSource.getRealTimeRankings(
            genre = genre,
            lastId = lastId,
            lastValue = lastValue,
            size = size,
        ).map { response ->
            CursorPage(
                cursor = response.cursor.toCursor(),
                items = response.animes.map { it.toAnime() },
            )
        }
    }

    override suspend fun getYearSeasonRankings(
        year: Int?,
        season: Int?,
        genre: String?,
        lastId: Long?,
        lastRank: String?,
        size: Int,
    ): Result<CursorPage<Anime>, DataError.Network> {
        return rankingNetworkDataSource.getYearSeasonRankings(
            year = year,
            season = season,
            genre = genre,
            lastId = lastId,
            lastRank = lastRank,
            size = size,
        ).map { response ->
            CursorPage(
                cursor = response.cursor.toCursor(),
                items = response.animes.map { it.toAnime() },
            )
        }
    }

    override suspend fun getAllTimeRankings(
        genre: String?,
        lastId: Long?,
        lastRank: String?,
        size: Int,
    ): Result<CursorPage<Anime>, DataError.Network> {
        return rankingNetworkDataSource.getAllTimeRankings(
            genre = genre,
            lastId = lastId,
            lastRank = lastRank,
            size = size,
        ).map { response ->
            CursorPage(
                cursor = response.cursor.toCursor(),
                items = response.animes.map { it.toAnime() },
            )
        }
    }
}
