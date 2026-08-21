package com.jparkbro.core.network.auth

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.auth.dto.AuthProvider
import com.jparkbro.core.network.auth.dto.OAuthLoginResponse

interface AuthNetworkDataSource {
    /** 소셜 로그인 - `POST /oauth/{provider}/callback`. */
    suspend fun loginWithOAuth(provider: AuthProvider, code: String): Result<OAuthLoginResponse, DataError.Network>

    /** 이메일 로그인 - `POST /users/login`. */
    suspend fun loginWithEmail(email: String, password: String): Result<OAuthLoginResponse, DataError.Network>

    /** 이메일 회원가입 - `POST /users/signup`. */
    suspend fun signUpWithEmail(
        email: String,
        password: String,
        termsAndConditions: Boolean,
    ): Result<OAuthLoginResponse, DataError.Network>

    /** 이메일 인증코드 발송 - `POST /auth/email/send`. */
    suspend fun sendEmailVerification(email: String): Result<Unit, DataError.Network>

    /** 이메일 인증코드 확인 - `POST /auth/email/verify`. */
    suspend fun verifyEmailCode(email: String, code: String): Result<Unit, DataError.Network>

    /** 비밀번호 재설정 - `PATCH /auth/password/reset`. */
    suspend fun resetPassword(
        email: String,
        newPassword: String,
        checkNewPassword: String,
    ): Result<Unit, DataError.Network>
}
