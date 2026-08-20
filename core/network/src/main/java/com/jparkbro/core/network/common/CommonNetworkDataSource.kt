package com.jparkbro.core.network.common

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result

interface CommonNetworkDataSource {
    /** 필터용 메타데이터(시즌/장르/타입 등) - `GET /animes/meta-data-group`. */
    suspend fun getMetadata(): Result<MetaDataResponse, DataError.Network>
}
