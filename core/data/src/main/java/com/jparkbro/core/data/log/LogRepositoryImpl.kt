package com.jparkbro.core.data.log

import com.jparkbro.core.network.log.LogNetworkDataSource

class LogRepositoryImpl(
    private val logNetworkDataSource: LogNetworkDataSource,
) : LogRepository {

    override suspend fun sendLog(url: String) = logNetworkDataSource.sendLog(url)
}
