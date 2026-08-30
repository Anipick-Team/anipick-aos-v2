package com.jparkbro.core.network

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.content.PartData
import io.ktor.util.network.UnresolvedAddressException
import io.ktor.utils.io.CancellationException
import kotlinx.serialization.SerializationException
import timber.log.Timber

/** GET 요청을 보내고 결과를 [Result]로 감싼다 */
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

/** 바디 없는 POST 버전. 나머지는 [HttpClient.post] 참고. */
suspend inline fun <reified Response : Any> HttpClient.post(
    route: String,
): Result<Response, DataError.Network> {
    return safeCall {
        post {
            url(constructRoute(route))
        }
    }
}

/** PATCH 버전. 나머지는 [HttpClient.post] 참고. */
suspend inline fun <reified Request, reified Response : Any> HttpClient.patch(
    route: String,
    body: Request
): Result<Response, DataError.Network> {
    return safeCall {
        patch {
            url(constructRoute(route))
            setBody(body)
        }
    }
}

/** 바디 없는 PATCH 버전. 나머지는 [HttpClient.patch] 참고. */
suspend inline fun <reified Response : Any> HttpClient.patch(
    route: String,
): Result<Response, DataError.Network> {
    return safeCall {
        patch {
            url(constructRoute(route))
        }
    }
}

/** PUT 버전. 나머지는 [HttpClient.post] 참고. */
suspend inline fun <reified Request, reified Response : Any> HttpClient.put(
    route: String,
    body: Request
): Result<Response, DataError.Network> {
    return safeCall {
        put {
            url(constructRoute(route))
            setBody(body)
        }
    }
}

/** multipart/form-data 요청(이미지 업로드 등)을 보낸다 */
suspend inline fun <reified Response : Any> HttpClient.postMultipart(
    route: String,
    formData: List<PartData>,
): Result<Response, DataError.Network> {
    return safeCall {
        post {
            url(constructRoute(route))
            setBody(MultiPartFormDataContent(formData))
        }
    }
}

/** 통신 자체의 성공/실패를 [DataError.Network]로 변환한다 */
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

/** 응답 body의 [ApiResponse.code]로 비즈니스 성공/실패 판단
 *  code == 200: result 반환, 그 외: [DataError.Network.Api]로 변환 */
suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, DataError.Network> {
    val body = response.body<ApiResponse<T>>()

    return when {
        body.code == 200 -> {
            if (T::class == Unit::class) {
                @Suppress("UNCHECKED_CAST")
                Result.Success(Unit as T)
            } else {
                @Suppress("UNCHECKED_CAST")
                Result.Success(body.result as T)
            }
        }
        else -> {
            Timber.e("API 실패: code=${body.code}, value=${body.errorValue}, errorReason=${body.errorReason}")
            Result.Failure(
                DataError.Network.Api(code = body.code, message = body.errorValue, reason = body.errorReason)
            )
        }
    }
}

/** [route]를 [BuildConfig.BASE_URL]과 이어붙여 완전한 URL 생성 */
fun constructRoute(route: String): String {
    return when {
        route.contains(BuildConfig.BASE_URL) -> route
        route.startsWith("/") -> BuildConfig.BASE_URL + route
        else -> BuildConfig.BASE_URL + "/$route"
    }
}