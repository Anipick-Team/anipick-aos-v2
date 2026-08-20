package com.jparkbro.core.common.result

/** 데이터 계층(네트워크, 로컬 DB 등)에서 발생하는 에러 */
sealed interface DataError : Error {

    /** Ktor 요청 과정에서 발생할 수 있는 에러 */
    sealed interface Network : DataError {
        // 현재는 인프라(게이트웨이/타임아웃 등) 레벨 HTTP status 분기를 쓰지 않아서 아래 케이스들은 미사용 상태.
        // HttpClientExt.kt의 주석 처리된 분기를 다시 켜면 같이 쓰인다.
        data object REQUEST_TIMEOUT : Network
        data object UNAUTHORIZED : Network
        data object CONFLICT : Network
        data object TOO_MANY_REQUESTS : Network
        data object NO_INTERNET : Network
        data object PAYLOAD_TOO_LARGE : Network
        data object SERVER_ERROR : Network
        data object SERIALIZATION : Network
        data object UNKNOWN : Network

        /** 비즈니스 실패 응답 */
        data class Api(val code: Int, val message: String?, val reason: String?) : Network
    }

    /** 로컬 저장소(Room, DataStore 등)에서 발생할 수 있는 에러 */
    enum class Local : DataError {
        DISK_FULL,
        UNKNOWN,
    }
}
