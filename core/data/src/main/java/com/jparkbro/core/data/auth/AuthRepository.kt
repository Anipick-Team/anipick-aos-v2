package com.jparkbro.core.data.auth

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result

interface AuthRepository {
    suspend fun loginWithKakao(accessToken: String): Result<Unit, DataError.Network>
    suspend fun loginWithGoogle(idToken: String): Result<Unit, DataError.Network>
}
