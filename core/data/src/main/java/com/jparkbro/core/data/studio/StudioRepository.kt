package com.jparkbro.core.data.studio

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.studio.StudioAnimePage

/** 스튜디오 관련 데이터를 읽어오는 인터페이스 */
interface StudioRepository {
    /** 스튜디오 상세 애니 목록 - `GET /studios/{studioId}/animes`. */
    suspend fun getStudioAnimes(
        studioId: Long,
        lastId: Long? = null,
        lastValue: String? = null,
        size: Int = 18,
    ): Result<StudioAnimePage, DataError.Network>
}
