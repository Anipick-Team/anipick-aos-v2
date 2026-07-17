package com.jparkbro.core.common.result

/**
 * 성공(D)과 실패(E)를 명시적인 타입으로 표현하는 결과 래퍼.
 * 예외를 던지는 대신 이 타입을 반환해서, 호출부가 when으로 성공/실패를 분기 처리하도록 강제한다.
 *
 * @param D 성공 시 반환되는 데이터 타입
 * @param E 실패 시 반환되는 에러 타입 ([Error]를 구현해야 함, 예: [DataError])
 */
sealed interface Result<out D, out E : Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Failure<out E : Error>(val error: E) : Result<Nothing, E>
}

/**
 * Result의 데이터를 변환한다. 실패(Failure)는 그대로 통과시킨다.
 */
inline fun <D, E : Error, R> Result<D, E>.map(map: (D) -> R): Result<R, E> {
    return when (this) {
        is Result.Success -> Result.Success(map(data))
        is Result.Failure -> this
    }
}

/**
 * 성공 데이터를 버리고 [EmptyResult]로 변환한다. 실패(Failure)는 그대로 통과시킨다.
 */
fun <D, E : Error> Result<D, E>.asEmptyDataResult(): EmptyResult<E> {
    return map {}
}

/**
 * 성공 데이터가 필요 없을 때(로그아웃, 삭제 등) [Unit]으로 축소한 결과 타입.
 */
typealias EmptyResult<E> = Result<Unit, E>

/**
 * 성공일 때만 부수 효과(로깅, 캐싱 등)를 실행하고 원래 Result를 그대로 반환한다.
 */
inline fun <D, E : Error> Result<D, E>.onSuccess(action: (D) -> Unit): Result<D, E> {
    if (this is Result.Success) action(data)
    return this
}

/**
 * 실패일 때만 부수 효과(로깅, 에러 처리 등)를 실행하고 원래 Result를 그대로 반환한다.
 */
inline fun <D, E : Error> Result<D, E>.onFailure(action: (E) -> Unit): Result<D, E> {
    if (this is Result.Failure) action(error)
    return this
}