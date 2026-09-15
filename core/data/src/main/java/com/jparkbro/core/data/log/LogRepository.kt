package com.jparkbro.core.data.log

interface LogRepository {
    /** 클릭/노출 로그 URL을 그대로 호출 */
    suspend fun sendLog(url: String)
}
