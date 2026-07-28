package com.jparkbro.core.network.auth

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.post
import io.ktor.client.HttpClient

class KtorAuthNetworkDataSource(
    private val httpClient: HttpClient,
) : AuthNetworkDataSource {

    override suspend fun loginWithOAuth(
        provider: AuthProvider,
        code: String,
    ): Result<OAuthLoginResponse, DataError.Network> {
        return httpClient.post(
            route = "/oauth/${provider.name}/callback",
            body = OAuthLoginRequest(code = code),
        )
    }
}
