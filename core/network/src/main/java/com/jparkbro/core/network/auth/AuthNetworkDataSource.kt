package com.jparkbro.core.network.auth

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result

interface AuthNetworkDataSource {
    suspend fun loginWithOAuth(provider: AuthProvider, code: String): Result<OAuthLoginResponse, DataError.Network>
    suspend fun loginWithEmail(email: String, password: String): Result<OAuthLoginResponse, DataError.Network>
    suspend fun signUpWithEmail(
        email: String,
        password: String,
        termsAndConditions: Boolean,
    ): Result<OAuthLoginResponse, DataError.Network>
    suspend fun sendEmailVerification(email: String): Result<Unit, DataError.Network>
    suspend fun verifyEmailCode(email: String, code: String): Result<Unit, DataError.Network>
}
