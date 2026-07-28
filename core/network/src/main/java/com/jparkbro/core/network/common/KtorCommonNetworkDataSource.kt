package com.jparkbro.core.network.common

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.get
import io.ktor.client.HttpClient

class KtorCommonNetworkDataSource(
    private val httpClient: HttpClient,
) : CommonNetworkDataSource {

    override suspend fun getMetadata(): Result<MetaDataResponse, DataError.Network> {
        return httpClient.get("animes/meta-data-group")
    }
}
