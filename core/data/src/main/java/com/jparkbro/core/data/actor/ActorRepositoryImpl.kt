package com.jparkbro.core.data.actor

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.model.actor.ActorDetailPage
import com.jparkbro.core.network.actor.ActorNetworkDataSource
import com.jparkbro.core.network.actor.dto.toActorDetailPage

class ActorRepositoryImpl(
    private val actorNetworkDataSource: ActorNetworkDataSource,
) : ActorRepository {

    override suspend fun getActorDetail(
        personId: Long,
        lastId: Long?,
        size: Int,
    ): Result<ActorDetailPage, DataError.Network> {
        return actorNetworkDataSource.getActorDetail(
            personId = personId,
            lastId = lastId,
            size = size,
        ).map { it.toActorDetailPage() }
    }

    override suspend fun likeActor(personId: Long): Result<Unit, DataError.Network> {
        return actorNetworkDataSource.likeActor(personId)
    }

    override suspend fun unlikeActor(personId: Long): Result<Unit, DataError.Network> {
        return actorNetworkDataSource.unlikeActor(personId)
    }
}
