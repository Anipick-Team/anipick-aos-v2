package com.jparkbro.core.data.common

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.Metadata

interface CommonRepository {
    suspend fun getMetadata(): Result<Metadata, DataError.Network>
}
