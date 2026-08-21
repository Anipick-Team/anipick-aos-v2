package com.jparkbro.core.data.series

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.model.anime.SeriesResult
import com.jparkbro.core.network.common.toCursor
import com.jparkbro.core.network.series.SeriesNetworkDataSource
import com.jparkbro.core.network.series.dto.toAnime

class SeriesRepositoryImpl(
    private val seriesNetworkDataSource: SeriesNetworkDataSource,
) : SeriesRepository {

    override suspend fun getAnimeSeries(
        animeId: Long,
        lastId: Long?,
        size: Int,
    ): Result<SeriesResult, DataError.Network> {
        return seriesNetworkDataSource.getAnimeSeries(
            animeId = animeId,
            lastId = lastId,
            size = size,
        ).map { response ->
            SeriesResult(
                count = response.count,
                cursor = response.cursor.toCursor(),
                animes = response.animes?.map { it.toAnime() },
            )
        }
    }
}
