package com.jparkbro.core.network.actor

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.actor.dto.ActorDetailResponse
import com.jparkbro.core.network.delete
import com.jparkbro.core.network.get
import com.jparkbro.core.network.post
import io.ktor.client.HttpClient

class KtorActorNetworkDataSource(
    private val httpClient: HttpClient,
) : ActorNetworkDataSource {

    override suspend fun getActorDetail(
        personId: Long,
        lastId: Long?,
        size: Int,
    ): Result<ActorDetailResponse, DataError.Network> {
        return httpClient.get(
            route = "/person/$personId",
            queryParameters = mapOf(
                "lastId" to lastId,
                "size" to size,
            ),
        )
    }

    override suspend fun likeActor(personId: Long): Result<Unit, DataError.Network> {
        return httpClient.post(route = "/persons/$personId/like")
    }

    override suspend fun unlikeActor(personId: Long): Result<Unit, DataError.Network> {
        return httpClient.delete(route = "/persons/$personId/like")
    }
}
