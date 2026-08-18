package com.jparkbro.core.network.user

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.user.dto.MyPageResponse
import com.jparkbro.core.network.user.dto.UserSettingResponse

interface UserNetworkDataSource {
    suspend fun getMyPage(): Result<MyPageResponse, DataError.Network>
    suspend fun getUserSetting(): Result<UserSettingResponse, DataError.Network>
    suspend fun updateNickname(nickname: String): Result<Unit, DataError.Network>
    suspend fun updateEmail(newEmail: String, password: String): Result<Unit, DataError.Network>
    suspend fun updatePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
    ): Result<Unit, DataError.Network>
    suspend fun withdraw(): Result<Unit, DataError.Network>
}
