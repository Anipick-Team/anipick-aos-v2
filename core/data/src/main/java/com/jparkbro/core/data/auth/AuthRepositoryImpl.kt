package com.jparkbro.core.data.auth

import com.jparkbro.core.common.auth.TokenProvider
import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.datastore.RecentAnimeDataStore
import com.jparkbro.core.datastore.RecentSearchDataStore
import com.jparkbro.core.datastore.UserDataStore
import com.jparkbro.core.network.auth.AuthNetworkDataSource
import com.jparkbro.core.network.auth.dto.AuthProvider

class AuthRepositoryImpl(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val tokenProvider: TokenProvider,
    private val userDataStore: UserDataStore,
    private val recentSearchDataStore: RecentSearchDataStore,
    private val recentAnimeDataStore: RecentAnimeDataStore,
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
                userDataStore.saveUser(
                    userId = response.userId,
                    nickname = response.nickname,
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
                userDataStore.saveUser(
                    userId = response.userId,
                    nickname = response.nickname,
                )
                response.reviewCompletedYn
            }
    }

    override suspend fun sendEmailVerification(email: String): Result<Unit, DataError.Network> {
        return authNetworkDataSource.sendEmailVerification(email)
    }

    override suspend fun verifyEmailCode(email: String, code: String): Result<Unit, DataError.Network> {
        return authNetworkDataSource.verifyEmailCode(email, code)
    }

    override suspend fun resetPassword(
        email: String,
        newPassword: String,
        checkNewPassword: String,
    ): Result<Unit, DataError.Network> {
        return authNetworkDataSource.resetPassword(email, newPassword, checkNewPassword)
    }

    override suspend fun clearLocalData() {
        tokenProvider.clearTokens()
        userDataStore.clearUser()
        recentSearchDataStore.clearRecentSearches()
        recentAnimeDataStore.clearRecentAnimeId()
    }

    private suspend fun login(provider: AuthProvider, code: String): Result<Boolean, DataError.Network> {
        return authNetworkDataSource.loginWithOAuth(provider, code)
            .map { response ->
                tokenProvider.saveTokens(
                    accessToken = response.token.accessToken,
                    refreshToken = response.token.refreshToken,
                )
                userDataStore.saveUser(
                    userId = response.userId,
                    nickname = response.nickname,
                )
                response.reviewCompletedYn
            }
    }
}
