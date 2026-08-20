package com.jparkbro.core.data.common

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.metadata.Metadata

/** 공통으로 쓰이는 데이터를 읽어오는 인터페이스 */
interface CommonRepository {
    /** 장르/시즌 메타데이터 - `GET /animes/meta-data-group`. */
    suspend fun getMetadata(): Result<Metadata, DataError.Network>
}
