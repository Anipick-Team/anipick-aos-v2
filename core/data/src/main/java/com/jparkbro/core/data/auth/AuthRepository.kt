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
}
