package com.jparkbro.core.network

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import io.ktor.utils.io.CancellationException
import kotlinx.serialization.SerializationException
import timber.log.Timber

/**
 * GET 요청을 보내고 결과를 [Result]로 감싼다. 예외를 던지지 않고 [safeCall]이 실패를 [DataError.Network]로 변환한다.
 */
suspend inline fun <reified Response : Any> HttpClient.get(
    route: String,
    queryParameters: Map<String, Any?> = mapOf()
): Result<Response, DataError.Network> {
    return safeCall {
        get {
            url(constructRoute(route))
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }
}

/** DELETE 버전. 나머지는 [HttpClient.get] 참고. */
suspend inline fun <reified Response : Any> HttpClient.delete(
    route: String,
    queryParameters: Map<String, Any?> = mapOf()
): Result<Response, DataError.Network> {
    return safeCall {
        delete {
            url(constructRoute(route))
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }
}

/** POST 버전. 요청 바디(body)를 함께 직렬화해서 보낸다. */
suspend inline fun <reified Request, reified Response : Any> HttpClient.post(
    route: String,
    body: Request
): Result<Response, DataError.Network> {
    return safeCall {
        post {
            url(constructRoute(route))
            setBody(body)
        }
    }
}

/**
 * Ktor 요청 실행과 응답 파싱 중 발생할 수 있는 예외를 잡아서 [DataError.Network]로 변환한다.
 * [CancellationException]은 코루틴 취소 메커니즘이 정상 동작해야 하므로 잡되 다시 던진다.
 */
suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T, DataError.Network> {
    return try {
        responseToResult(execute())
    } catch (e: UnresolvedAddressException) {
        e.printStackTrace()
        Result.Failure(DataError.Network.NO_INTERNET)
    } catch (e: SerializationException) {
        e.printStackTrace()
        Result.Failure(DataError.Network.SERIALIZATION)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        e.printStackTrace()
        Result.Failure(DataError.Network.UNKNOWN)
    }
}

/**
 * HTTP status가 아니라 [ApiResponse.code]로 성공/실패를 판단한다. 이 서버는 비즈니스 실패도 HTTP 200으로
 * 감싸서 {code, value} 형태로 내려주므로, code == 200일 때만 성공이고 그 외(100, 101 ...)는 전부 실패다.
 * 실패 시 [ApiResponse.value](실패 사유)를 [DataError.Network.Api]에 그대로 실어서, 호출부가 다이얼로그나
 * 에러 메시지로 바로 보여줄 수 있게 한다.
 */
suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, DataError.Network> {
    val body = response.body<ApiResponse<T>>()

    if (body.code != 200) {
        Timber.e("API 실패: code=${body.code}, value=${body.errorValue}, errorReason=${body.errorReason}")
        return Result.Failure(
            DataError.Network.Api(code = body.code, message = body.errorValue, reason = body.errorReason)
        )
    }

    val result = body.result
    return if (result != null) {
        Result.Success(result)
    } else {
        Timber.e("API 실패 (code=200인데 result 없음): value=${body.value}")
        Result.Failure(DataError.Network.UNKNOWN)
    }
}

/**
 * [route]를 [BuildConfig.BASE_URL]과 이어붙여 완전한 URL을 만든다.
 * 문자열을 직접 조합하기 때문에 route에 선행 슬래시가 있든 없든 항상 정확한 경로가 만들어진다
 * (Ktor의 URLBuilder 병합 규칙에 기대지 않음).
 */
fun constructRoute(route: String): String {
    return when {
        route.contains(BuildConfig.BASE_URL) -> route
        route.startsWith("/") -> BuildConfig.BASE_URL + route
        else -> BuildConfig.BASE_URL + "/$route"
    }
}