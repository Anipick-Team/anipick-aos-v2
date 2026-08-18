package com.jparkbro.core.data.auth

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result

interface AuthRepository {

    suspend fun loginWithKakao(accessToken: String): Result<Boolean, DataError.Network>
    suspend fun loginWithGoogle(idToken: String): Result<Boolean, DataError.Network>
    suspend fun loginWithEmail(email: String, password: String): Result<Boolean, DataError.Network>
    suspend fun signUpWithEmail(
        email: String,
        password: String,
        termsAndConditions: Boolean,
    ): Result<Boolean, DataError.Network>
    suspend fun sendEmailVerification(email: String): Result<Unit, DataError.Network>
    suspend fun verifyEmailCode(email: String, code: String): Result<Unit, DataError.Network>
    suspend fun resetPassword(
        email: String,
        newPassword: String,
        checkNewPassword: String,
    ): Result<Unit, DataError.Network>

    /** 로그아웃/회원탈퇴처럼 로컬 세션을 완전히 초기화해야 할 때, 이 기기에 저장된 datastore 정보를 전부 지운다. */
    suspend fun clearLocalData()
}
