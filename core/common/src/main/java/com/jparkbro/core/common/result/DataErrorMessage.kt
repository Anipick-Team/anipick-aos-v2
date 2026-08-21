package com.jparkbro.core.common.result

private const val NO_INTERNET_MESSAGE = "네트워크 연결을 확인해주세요."
private const val UNKNOWN_ERROR_MESSAGE = "알 수 없는 오류가 발생했습니다."

/** 실패를 사용자에게 그대로 보여줄 문구로 변환한다 - 서버가 내려준 노출 메시지([DataError.Network.Api.message])가
 *  있으면 그대로 쓰고, 없거나 네트워크 연결 자체 문제면 상황별 기본 문구로 대체한다.
 *
 *  `code`별로 다른 동작(다이얼로그, 특정 입력 필드 에러 등)이 필요한 곳은 그 `is Api` 분기를 직접 유지하고,
 *  나머지 공통 케이스(`NO_INTERNET`, 그 외)만 이 함수로 위임하면 된다. */
fun DataError.Network.toDisplayMessage(): String = when (this) {
    DataError.Network.NO_INTERNET -> NO_INTERNET_MESSAGE
    is DataError.Network.Api -> message ?: UNKNOWN_ERROR_MESSAGE
    else -> UNKNOWN_ERROR_MESSAGE
}
