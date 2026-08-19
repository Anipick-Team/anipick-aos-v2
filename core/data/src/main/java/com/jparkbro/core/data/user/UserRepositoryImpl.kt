package com.jparkbro.core.data.user

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.data.auth.AuthRepository
import com.jparkbro.core.datastore.UserDataStore
import com.jparkbro.core.model.mypage.MyPageProfile
import com.jparkbro.core.model.user.UserSetting
import com.jparkbro.core.network.image.ImageNetworkDataSource
import com.jparkbro.core.network.user.UserNetworkDataSource
import com.jparkbro.core.network.user.dto.toMyPageProfile
import com.jparkbro.core.network.user.dto.toUserSetting
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userNetworkDataSource: UserNetworkDataSource,
    private val imageNetworkDataSource: ImageNetworkDataSource,
    private val userDataStore: UserDataStore,
    private val authRepository: AuthRepository,
) : UserRepository {

    override val nickname: Flow<String?> = userDataStore.nickname
    override val email: Flow<String?> = userDataStore.email

    override suspend fun getMyPage(): Result<MyPageProfile, DataError.Network> {
        return userNetworkDataSource.getMyPage().map { response ->
            response.toMyPageProfile()
        }
    }

    override suspend fun getUserSetting(): Result<UserSetting, DataError.Network> {
        return userNetworkDataSource.getUserSetting()
            .map { response -> response.toUserSetting() }
            .onSuccess { setting ->
                // 서버 원본 값으로 로컬 캐시를 갱신 -> nickname/email Flow 구독자(SettingMain 등)에 반영된다.
                userDataStore.saveNickname(setting.nickname)
                userDataStore.saveEmail(setting.email)
            }
    }

    override suspend fun updateNickname(nickname: String): Result<Unit, DataError.Network> {
        return userNetworkDataSource.updateNickname(nickname)
            .onSuccess { userDataStore.saveNickname(nickname) }
    }

    override suspend fun updateEmail(newEmail: String, password: String): Result<Unit, DataError.Network> {
        return userNetworkDataSource.updateEmail(newEmail, password)
            .onSuccess { userDataStore.saveEmail(newEmail) }
    }

    override suspend fun updatePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
    ): Result<Unit, DataError.Network> {
        return userNetworkDataSource.updatePassword(currentPassword, newPassword, confirmNewPassword)
    }

    override suspend fun withdraw(): Result<Unit, DataError.Network> {
        return userNetworkDataSource.withdraw()
            .onSuccess { authRepository.clearLocalData() }
    }

    override suspend fun updateProfileImage(
        imageBytes: ByteArray,
        fileName: String,
        mimeType: String,
    ): Result<Long, DataError.Network> {
        return imageNetworkDataSource.updateProfileImage(imageBytes, fileName, mimeType)
            .map { it.imageId }
    }
}
