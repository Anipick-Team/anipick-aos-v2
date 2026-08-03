package com.jparkbro.core.data.common

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.model.Metadata
import com.jparkbro.core.network.common.CommonNetworkDataSource
import com.jparkbro.core.network.common.toMetadata
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CommonRepositoryImpl(
    private val commonNetworkDataSource: CommonNetworkDataSource,
) : CommonRepository {

    private val mutex = Mutex()
    private var cachedMetadata: Metadata? = null

    override suspend fun getMetadata(): Result<Metadata, DataError.Network> {
        cachedMetadata?.let { return Result.Success(it) }

        return mutex.withLock {
            cachedMetadata?.let { return@withLock Result.Success(it) }

            commonNetworkDataSource.getMetadata()
                .map { response -> response.toMetadata() }
                .onSuccess { metadata -> cachedMetadata = metadata }
        }
    }
}
