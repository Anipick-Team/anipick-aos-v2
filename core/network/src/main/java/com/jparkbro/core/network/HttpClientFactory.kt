package com.jparkbro.core.network

import com.jparkbro.core.common.auth.TokenProvider
import com.jparkbro.core.network.auth.TokenRefreshResponse
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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import timber.log.Timber

/** 이 서버는 토큰이 무효해도 HTTP 상태는 200으로 내려주고, body의 code로만 실패를 알려준다.
 *  (예: {"code":119,"errorReason":"토큰 값이 유효 X",...}) */
private const val TOKEN_INVALID_CODE = 119

/**
 * 앱 전역에서 쓰는 [HttpClient]를 생성하는 팩토리. Koin [di.networkModule]에서 싱글턴으로 등록된다.
 *
 * @param tokenProvider 요청에 붙일 인증 토큰을 읽어오는 인터페이스. 실제 구현(core:datastore)은 모르고,
 *                       Koin이 런타임에 알맞은 구현체를 연결해준다.
 */
class HttpClientFactory(
    private val tokenProvider: TokenProvider,
) {

    fun build(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.d(message)
                    }
                }
                level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
            }

            install(Auth) {
                reAuthorizeOnResponse { response ->
                    if (response.status == HttpStatusCode.Unauthorized) return@reAuthorizeOnResponse true
                    if (!response.status.isSuccess()) return@reAuthorizeOnResponse false

                    val code = try {
                        response.body<ApiResponse<JsonElement>>().code
                    } catch (e: Exception) {
                        return@reAuthorizeOnResponse false
                    }
                    code == TOKEN_INVALID_CODE
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

                        val response = client.post(constructRoute("/tokens/refresh")) {
                            markAsRefreshTokenRequest()
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
