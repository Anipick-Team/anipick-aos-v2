package com.jparkbro.core.data.series

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.SeriesResult

/** 시리즈 관련 데이터를 읽어오는 인터페이스 */
interface SeriesRepository {
    /** 애니 상세 "정보" 탭의 시리즈 전체보기 - `GET /animes/{animeId}/series`. */
    suspend fun getAnimeSeries(
        animeId: Long,
        lastId: Long? = null,
        size: Int = 18,
    ): Result<SeriesResult, DataError.Network>
}
