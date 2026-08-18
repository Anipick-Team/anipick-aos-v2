package com.jparkbro.core.network.user

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.get
import com.jparkbro.core.network.patch
import com.jparkbro.core.network.put
import com.jparkbro.core.network.user.dto.EmailUpdateRequest
import com.jparkbro.core.network.user.dto.MyPageResponse
import com.jparkbro.core.network.user.dto.NicknameUpdateRequest
import com.jparkbro.core.network.user.dto.PasswordUpdateRequest
import com.jparkbro.core.network.user.dto.UserSettingResponse
import io.ktor.client.HttpClient

class KtorUserNetworkDataSource(
    private val httpClient: HttpClient,
) : UserNetworkDataSource {

    override suspend fun getMyPage(): Result<MyPageResponse, DataError.Network> {
        return httpClient.get(
            route = "/mypage"
        )
    }

    override suspend fun getUserSetting(): Result<UserSettingResponse, DataError.Network> {
        return httpClient.get(
            route = "/setting/view"
        )
    }

    override suspend fun updateNickname(nickname: String): Result<Unit, DataError.Network> {
        return httpClient.patch(
            route = "/setting/nickname",
            body = NicknameUpdateRequest(nickname = nickname),
        )
    }

    override suspend fun updateEmail(newEmail: String, password: String): Result<Unit, DataError.Network> {
        return httpClient.put(
            route = "/setting/email",
            body = EmailUpdateRequest(newEmail = newEmail, password = password),
        )
    }

    override suspend fun updatePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
    ): Result<Unit, DataError.Network> {
        return httpClient.patch(
            route = "/setting/password",
            body = PasswordUpdateRequest(
                currentPassword = currentPassword,
                newPassword = newPassword,
                confirmNewPassword = confirmNewPassword,
            ),
        )
    }

    override suspend fun withdraw(): Result<Unit, DataError.Network> {
        return httpClient.patch(
            route = "/setting/withdrawal",
        )
    }
}
