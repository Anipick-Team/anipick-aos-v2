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

    /** 배우 찜하기 - `POST /persons/{personId}/like`. */
    suspend fun likeActor(personId: Long): Result<Unit, DataError.Network>

    /** 배우 찜 취소 - `DELETE /persons/{personId}/like`. */
    suspend fun unlikeActor(personId: Long): Result<Unit, DataError.Network>
}
