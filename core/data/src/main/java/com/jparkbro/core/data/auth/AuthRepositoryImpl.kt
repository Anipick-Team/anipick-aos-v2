package com.jparkbro.core.data.auth

import com.jparkbro.core.common.auth.TokenProvider
import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.network.auth.AuthNetworkDataSource
import com.jparkbro.core.network.auth.AuthProvider

class AuthRepositoryImpl(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val tokenProvider: TokenProvider,
) : AuthRepository {

    override suspend fun loginWithKakao(accessToken: String): Result<Boolean, DataError.Network> {
        return login(AuthProvider.KAKAO, accessToken)
    }

    override suspend fun loginWithGoogle(idToken: String): Result<Boolean, DataError.Network> {
        return login(AuthProvider.GOOGLE, idToken)
    }

    override suspend fun loginWithEmail(email: String, password: String): Result<Boolean, DataError.Network> {
        return authNetworkDataSource.loginWithEmail(email, password)
            .map { response ->
                tokenProvider.saveTokens(
                    accessToken = response.token.accessToken,
                    refreshToken = response.token.refreshToken,
                )
                response.reviewCompletedYn
            }
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        termsAndConditions: Boolean,
    ): Result<Boolean, DataError.Network> {
        return authNetworkDataSource.signUpWithEmail(email, password, termsAndConditions)
            .map { response ->
                tokenProvider.saveTokens(
                    accessToken = response.token.accessToken,
                    refreshToken = response.token.refreshToken,
                )
                response.reviewCompletedYn
            }
    }

    private suspend fun login(provider: AuthProvider, code: String): Result<Boolean, DataError.Network> {
        return authNetworkDataSource.loginWithOAuth(provider, code)
            .map { response ->
                tokenProvider.saveTokens(
                    accessToken = response.token.accessToken,
                    refreshToken = response.token.refreshToken,
                )
                response.reviewCompletedYn
            }
    }
}
