package com.jparkbro.core.network.explore

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.explore.dto.ExploreAnimesResponse

interface ExploreNetworkDataSource {
    /** 탐색 화면 애니 목록 - `GET /explore/animes`. */
    suspend fun getExploreAnimes(
        year: Int? = null,
        season: Int? = null,
        genres: List<Long>? = null,
        genreOp: String? = null,
        type: String? = null,
        sort: String? = null,
        lastId: Long? = null,
        lastValue: String? = null,
        size: Int = 18,
    ): Result<ExploreAnimesResponse, DataError.Network>
}
