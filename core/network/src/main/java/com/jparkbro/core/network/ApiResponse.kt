package com.jparkbro.core.network

import kotlinx.serialization.Serializable

/** 백엔드 공통 응답 봉투. 성공: [result], 실패: [errorReason]/[errorValue] */
@Serializable
data class ApiResponse<T>(
    val code: Int,
    val value: String,
    val result: T? = null,
    val errorReason: String? = null,
    val errorValue: String? = null,
)
