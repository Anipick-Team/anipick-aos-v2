package com.jparkbro.core.common.result

/** 성공(D)/실패(E)를 명시적인 타입으로 표현하는 결과 래퍼 */
sealed interface Result<out D, out E : Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Failure<out E : Error>(val error: E) : Result<Nothing, E>
}

/** Result의 데이터 변환, 실패는 그대로 통과 */
inline fun <D, E : Error, R> Result<D, E>.map(map: (D) -> R): Result<R, E> {
    return when (this) {
        is Result.Success -> Result.Success(map(data))
        is Result.Failure -> this
    }
}

/** 성공 데이터를 버리고 [EmptyResult]로 변환, 실패는 그대로 통과 */
fun <D, E : Error> Result<D, E>.asEmptyDataResult(): EmptyResult<E> {
    return map {}
}

/** 성공 데이터가 필요 없을 때 [Unit]으로 축소한 결과 타입 */
typealias EmptyResult<E> = Result<Unit, E>

/** 성공일 때만 부수 효과 실행, 원래 Result 그대로 반환 */
inline fun <D, E : Error> Result<D, E>.onSuccess(action: (D) -> Unit): Result<D, E> {
    if (this is Result.Success) action(data)
    return this
}

/** 실패일 때만 부수 효과 실행, 원래 Result 그대로 반환 */
inline fun <D, E : Error> Result<D, E>.onFailure(action: (E) -> Unit): Result<D, E> {
    if (this is Result.Failure) action(error)
    return this
}
