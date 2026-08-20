package com.jparkbro.core.common.auth

import kotlinx.coroutines.flow.Flow

/** 인증 토큰 읽고 쓰는 인터페이스 */
interface TokenProvider {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun clearTokens()

    /** refreshToken 유효 여부 확인 */
    suspend fun isRefreshTokenValid(): Boolean

    /** 로그인 상태 실시간 감지 */
    val isLoggedIn: Flow<Boolean>
}
