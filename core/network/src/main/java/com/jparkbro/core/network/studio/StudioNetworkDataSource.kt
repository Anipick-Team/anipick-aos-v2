package com.jparkbro.core.network.studio

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.studio.dto.StudioAnimesResponse

interface StudioNetworkDataSource {
    /** 스튜디오 상세 - 제작 애니 목록 - `GET /studios/{studioId}/animes`. */
    suspend fun getStudioAnimes(
        studioId: Long,
        lastId: Long? = null,
        lastValue: String? = null,
        size: Int = 18,
    ): Result<StudioAnimesResponse, DataError.Network>
}
