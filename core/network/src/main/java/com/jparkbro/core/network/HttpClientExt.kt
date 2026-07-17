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
 * Ktor 요청 실행 중 발생할 수 있는 예외를 잡아서 [DataError.Network]로 변환한다.
 * [CancellationException]은 코루틴 취소 메커니즘이 정상 동작해야 하므로 잡되 다시 던진다.
 */
suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T, DataError.Network> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        e.printStackTrace()
        return Result.Failure(DataError.Network.NO_INTERNET)
    } catch (e: SerializationException) {
        e.printStackTrace()
        return Result.Failure(DataError.Network.SERIALIZATION)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        e.printStackTrace()
        return Result.Failure(DataError.Network.UNKNOWN)
    }

    return responseToResult(response)
}

/** HTTP 상태 코드를 [DataError.Network] 케이스로 매핑한다. 2xx는 응답 바디를 [T]로 역직렬화해서 성공 처리. */
suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, DataError.Network> {
    return when (response.status.value) {
        in 200..299 -> Result.Success(response.body<T>())
        401 -> Result.Failure(DataError.Network.UNAUTHORIZED)
        408 -> Result.Failure(DataError.Network.REQUEST_TIMEOUT)
        409 -> Result.Failure(DataError.Network.CONFLICT)
        413 -> Result.Failure(DataError.Network.PAYLOAD_TOO_LARGE)
        429 -> Result.Failure(DataError.Network.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Failure(DataError.Network.SERVER_ERROR)
        else -> Result.Failure(DataError.Network.UNKNOWN)
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