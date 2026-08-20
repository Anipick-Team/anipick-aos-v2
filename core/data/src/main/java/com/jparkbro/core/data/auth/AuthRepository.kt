package com.jparkbro.core.data.auth

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result

/** 인증/로그인 관련 데이터를 읽고 쓰는 인터페이스 */
interface AuthRepository {

    /** 카카오 로그인 - `POST /oauth/KAKAO/callback`. */
    suspend fun loginWithKakao(accessToken: String): Result<Boolean, DataError.Network>
    /** 구글 로그인 - `POST /oauth/GOOGLE/callback`. */
    suspend fun loginWithGoogle(idToken: String): Result<Boolean, DataError.Network>
    /** 이메일 로그인 - `POST /users/login`. */
    suspend fun loginWithEmail(email: String, password: String): Result<Boolean, DataError.Network>
    /** 이메일 회원가입 - `POST /users/signup`. */
    suspend fun signUpWithEmail(
        email: String,
        password: String,
        termsAndConditions: Boolean,
    ): Result<Boolean, DataError.Network>
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

    /** 로컬에 저장된 세션 데이터 전부 삭제 */
    suspend fun clearLocalData()
}
