package com.jparkbro.core.data.user

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.mypage.MyPageProfile
import com.jparkbro.core.model.user.UserSetting
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val nickname: Flow<String?>
    val email: Flow<String?>

    suspend fun getMyPage(): Result<MyPageProfile, DataError.Network>

    suspend fun getUserSetting(): Result<UserSetting, DataError.Network>

    suspend fun updateNickname(nickname: String): Result<Unit, DataError.Network>

    suspend fun updateEmail(newEmail: String, password: String): Result<Unit, DataError.Network>

    suspend fun updatePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
    ): Result<Unit, DataError.Network>

    suspend fun withdraw(): Result<Unit, DataError.Network>
}
