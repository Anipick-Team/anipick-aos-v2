package com.jparkbro.core.data.actor

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.actor.ActorDetailPage

/** 배우 관련 데이터를 읽어오는 인터페이스 */
interface ActorRepository {
    /** 배우 상세 - `GET /person/{personId}`. */
    suspend fun getActorDetail(
        personId: Long,
        lastId: Long? = null,
        size: Int = 18,
    ): Result<ActorDetailPage, DataError.Network>
}
