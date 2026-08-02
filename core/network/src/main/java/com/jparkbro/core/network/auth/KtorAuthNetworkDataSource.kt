package com.jparkbro.core.network.auth

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.patch
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

    override suspend fun loginWithEmail(
        email: String,
        password: String,
    ): Result<OAuthLoginResponse, DataError.Network> {
        return httpClient.post(
            route = "/users/login",
            body = EmailLoginRequest(email = email, password = password),
        )
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        termsAndConditions: Boolean,
    ): Result<OAuthLoginResponse, DataError.Network> {
        return httpClient.post(
            route = "/users/signup",
            body = EmailSignupRequest(email = email, password = password, termsAndConditions = termsAndConditions),
        )
    }

    override suspend fun sendEmailVerification(email: String): Result<Unit, DataError.Network> {
        return httpClient.post(
            route = "/auth/email/send",
            body = EmailVerificationSendRequest(email = email),
        )
    }

    override suspend fun verifyEmailCode(email: String, code: String): Result<Unit, DataError.Network> {
        return httpClient.post(
            route = "/auth/email/verify",
            body = EmailVerificationCheckRequest(email = email, code = code),
        )
    }

    override suspend fun resetPassword(
        email: String,
        newPassword: String,
        checkNewPassword: String,
    ): Result<Unit, DataError.Network> {
        return httpClient.patch(
            route = "/auth/password/reset",
            body = PasswordResetRequest(email = email, newPassword = newPassword, checkNewPassword = checkNewPassword),
        )
    }
}
