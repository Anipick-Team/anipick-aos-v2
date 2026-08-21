package com.jparkbro.core.data.explore

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.pagination.CursorPage

/** 탐색 관련 데이터를 읽어오는 인터페이스 */
interface ExploreRepository {
    /** 탐색 화면 전체 목록 - `GET /explore/animes`. */
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
    ): Result<CursorPage<Anime>, DataError.Network>
}
