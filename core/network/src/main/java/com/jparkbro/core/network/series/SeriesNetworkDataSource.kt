package com.jparkbro.core.network.series

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.series.dto.SeriesAnimesResponse

interface SeriesNetworkDataSource {
    /** 애니 상세 시리즈 전체보기 - `GET /animes/{animeId}/series`. */
    suspend fun getAnimeSeries(
        animeId: Long,
        lastId: Long? = null,
        size: Int = 18,
    ): Result<SeriesAnimesResponse, DataError.Network>
}
