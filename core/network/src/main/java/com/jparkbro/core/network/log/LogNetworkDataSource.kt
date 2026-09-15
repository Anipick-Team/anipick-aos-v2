package com.jparkbro.core.network.log

interface LogNetworkDataSource {
    /** 클릭/노출 로그 URL을 그대로 호출 - 응답 없음, 실패해도 무시 */
    suspend fun sendLog(url: String)
}
