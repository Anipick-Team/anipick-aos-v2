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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import timber.log.Timber

/** 토큰 무효를 뜻하는 응답 code */
private const val TOKEN_INVALID_CODE = 119

/** 앱 전역 [HttpClient] 생성 팩토리 */
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
