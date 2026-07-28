package com.jparkbro.core.network.common

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result

interface CommonNetworkDataSource {
    suspend fun getMetadata(): Result<MetaDataResponse, DataError.Network>
}
