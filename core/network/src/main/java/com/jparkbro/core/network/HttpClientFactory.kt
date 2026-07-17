package com.jparkbro.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

/**
 * 앱 전역에서 쓰는 [HttpClient]를 생성하는 팩토리. Koin [di.networkModule]에서 싱글턴으로 등록된다.
 */
class HttpClientFactory {

    fun build(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    // 서버 응답에 클라이언트가 모르는 필드가 추가돼도 역직렬화가 깨지지 않도록 허용
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(Logging) {
                // Ktor 기본 로거 대신 Timber로 출력해서 다른 로그와 포맷을 통일
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.d(message)
                    }
                }
                level = LogLevel.ALL
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
//                header("x-api-key", BuildConfig.API_KEY)
            }
        }
    }
}