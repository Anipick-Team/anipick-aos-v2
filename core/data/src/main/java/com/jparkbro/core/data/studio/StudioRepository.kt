package com.jparkbro.core.data.studio

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.studio.StudioAnimePage

interface StudioRepository {
    suspend fun getStudioAnimes(
        studioId: Long,
        lastId: Long? = null,
        lastValue: String? = null,
        size: Int = 18,
    ): Result<StudioAnimePage, DataError.Network>
}
