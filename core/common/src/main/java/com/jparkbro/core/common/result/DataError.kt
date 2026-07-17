package com.jparkbro.core.common.result

/**
 * 데이터 계층(네트워크, 로컬 DB 등)에서 발생하는 에러를 표현한다.
 * 원인이 되는 예외(Throwable)를 그대로 노출하지 않고, 케이스를 한정된 enum으로 분류해서
 * ViewModel/UI가 예외 타입을 몰라도 되게 한다.
 */
sealed interface DataError : Error {

    /** Ktor 요청 과정에서 발생할 수 있는 에러 */
    enum class Network : DataError {
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN,
    }

    /** 로컬 저장소(Room, DataStore 등)에서 발생할 수 있는 에러 */
    enum class Local : DataError {
        DISK_FULL,
        UNKNOWN,
    }
}
