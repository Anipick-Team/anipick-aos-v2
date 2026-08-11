package com.jparkbro.core.data.auth

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    /** 로그인한 유저 닉네임. 한 곳(datastore)만 바라보는 값이라, 어디서 바뀌든 이 Flow를 구독하는
     *  모든 화면(홈, 마이페이지 등)에 그대로 반영된다. */
    val nickname: Flow<String?>

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
}
