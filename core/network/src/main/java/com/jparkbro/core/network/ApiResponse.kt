package com.jparkbro.core.network

import kotlinx.serialization.Serializable

/**
 * 백엔드가 모든 응답을 감싸서 내려주는 공통 봉투.
 * 성공하면 [result]가 채워지고, 실패하면 [result]는 없이 [errorReason]/[errorValue]가 채워진다.
 */
@Serializable
data class ApiResponse<T>(
    val code: Int,
    val value: String,
    val result: T? = null,
    val errorReason: String? = null,
    val errorValue: String? = null,
)
