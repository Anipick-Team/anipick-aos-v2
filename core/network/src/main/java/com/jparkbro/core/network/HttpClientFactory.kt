package com.jparkbro.core.network

import com.jparkbro.core.common.auth.TokenProvider
import com.jparkbro.core.network.auth.dto.TokenRefreshResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import timber.log.Timber

/** 토큰 무효를 뜻하는 응답 code */
private const val TOKEN_INVALID_CODE = 119

/** 토큰 만료를 뜻하는 응답 code - accessToken이 순수 시간 만료로 죽었을 때 이 code로 내려온다 */
private const val TOKEN_EXPIRED_CODE = 121

/** Ktor 기본 Logger는 Android에서 SLF4J no-op이라 출력되지 않는다. Timber로 연결하고, DebugTree는 debug 빌드에서만 심긴다. */
private val TimberLogger = object : Logger {
    override fun log(message: String) {
        Timber.tag("Ktor").d(message)
    }
}

/** 모든 클라이언트가 공유하는 JSON 설정 - `coerceInputValues`는 켜지 않는다(dto-model-nullability 참고) */
private val ApiJson = Json {
    ignoreUnknownKeys = true
}

/** 앱 전역 [HttpClient] 생성 팩토리 */
class HttpClientFactory(
    private val tokenProvider: TokenProvider,
) {

    /** /tokens/refresh 전용 클라이언트 - [Auth] 플러그인이 없다.
     *  Ktor 3.1.3부터 markAsRefreshTokenRequest()로 표시한 요청은 Authorization 헤더를
     *  무조건 제거해버려서(https://github.com/ktorio/ktor/releases/tag/3.1.3), 리프레시 토큰을
     *  헤더로 보내야 하는 이 API 계약상 원래 client로는 refresh 요청 자체가 항상 401로 실패한다. */
    private val refreshHttpClient: HttpClient by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(ApiJson)
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }

    fun build(): HttpClient {
        return HttpClient(CIO) {
            if (BuildConfig.DEBUG) {
                install(Logging) {
                    logger = TimberLogger
                    level = LogLevel.ALL
                }
            }

            install(ContentNegotiation) {
                json(ApiJson)
            }

            install(Auth) {
                reAuthorizeOnResponse { response ->
                    if (response.status == HttpStatusCode.Unauthorized || response.status == HttpStatusCode.Forbidden) {
                        return@reAuthorizeOnResponse true
                    }
                    if (!response.status.isSuccess()) return@reAuthorizeOnResponse false

                    val code = try {
                        response.body<ApiResponse<JsonElement>>().code
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        return@reAuthorizeOnResponse false
                    }

                    code == TOKEN_INVALID_CODE || code == TOKEN_EXPIRED_CODE
                }

                bearer {
                    loadTokens {
                        val accessToken = tokenProvider.getAccessToken() ?: return@loadTokens null
                        val refreshToken = tokenProvider.getRefreshToken() ?: return@loadTokens null
                        BearerTokens(accessToken, refreshToken)
                    }
                    refreshTokens {
                        val refreshToken = oldTokens?.refreshToken ?: run {
                            tokenProvider.clearTokens()
                            return@refreshTokens null
                        }

                        val response = refreshHttpClient.post(constructRoute("/tokens/refresh")) {
                            header(HttpHeaders.Authorization, "Bearer $refreshToken")
                        }

                        val body = if (response.status.isSuccess()) {
                            response.body<ApiResponse<TokenRefreshResponse>>().result
                        } else {
                            null
                        }

                        if (body != null) {
                            tokenProvider.saveTokens(body.accessToken, body.refreshToken)
                            BearerTokens(body.accessToken, body.refreshToken)
                        } else {
                            tokenProvider.clearTokens()
                            null
                        }
                    }
                }
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }
}
