package com.jparkbro.core.network.actor

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.actor.dto.ActorDetailResponse

interface ActorNetworkDataSource {
    /** 배우 상세 - `GET /person/{personId}`. */
    suspend fun getActorDetail(
        personId: Long,
        lastId: Long? = null,
        size: Int = 18,
    ): Result<ActorDetailResponse, DataError.Network>

    /** 배우 찜하기 - `POST /persons/{personId}/like`. */
    suspend fun likeActor(personId: Long): Result<Unit, DataError.Network>

    /** 배우 찜 취소 - `DELETE /persons/{personId}/like`. */
    suspend fun unlikeActor(personId: Long): Result<Unit, DataError.Network>
}
