package com.jparkbro.core.common.auth

import kotlinx.coroutines.flow.Flow

/**
 * 인증 토큰을 읽고 쓰는 계약. 실제 저장 방식(DataStore 등)은 이 인터페이스를 모른다 —
 * [core:network]는 토큰을 읽기 위해, [core:data]는 로그인 후 토큰을 저장하기 위해 이 인터페이스만 의존하고,
 * 구현체는 core:datastore가 제공해서 Koin이 런타임에 연결한다.
 */
interface TokenProvider {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun clearTokens()

    /** refreshToken이 존재하고 아직 만료되지 않았는지 확인한다. accessToken은 만료됐어도
     *  refreshToken만 유효하면 이후 요청에서 Ktor Auth 플러그인이 알아서 갱신하므로 검사 대상이 아니다. */
    suspend fun isRefreshTokenValid(): Boolean

    /** 로그인 상태를 실시간으로 흘려보낸다. 앱 최상단에서 이 값이 true→false로 바뀌는 걸 감지해서
     *  (예: refreshToken도 만료되어 [clearTokens]가 호출된 경우) 자동으로 로그인 화면으로 이동시키는 데 쓴다. */
    val isLoggedIn: Flow<Boolean>
}
