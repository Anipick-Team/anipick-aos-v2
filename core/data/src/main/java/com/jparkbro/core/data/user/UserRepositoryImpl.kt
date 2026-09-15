package com.jparkbro.core.data.user

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.asEmptyDataResult
import com.jparkbro.core.common.result.map
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.data.auth.AuthRepository
import com.jparkbro.core.datastore.UserDataStore
import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.anime.AnimeWatchStatus
import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.model.mypage.MyCommunityComment
import com.jparkbro.core.model.mypage.MyPageProfile
import com.jparkbro.core.model.pagination.CursorPage
import com.jparkbro.core.model.review.Review
import com.jparkbro.core.model.user.UserSetting
import com.jparkbro.core.network.common.toCursor
import com.jparkbro.core.network.image.ImageNetworkDataSource
import com.jparkbro.core.network.image.toImageId
import com.jparkbro.core.network.user.UserNetworkDataSource
import com.jparkbro.core.network.user.dto.toActor
import com.jparkbro.core.network.user.dto.toAnime
import com.jparkbro.core.network.user.dto.toCommunityPost
import com.jparkbro.core.network.user.dto.toMyCommunityComment
import com.jparkbro.core.network.user.dto.toMyPageProfile
import com.jparkbro.core.network.user.dto.toReview
import com.jparkbro.core.network.user.dto.toUserSetting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class UserRepositoryImpl(
    private val userNetworkDataSource: UserNetworkDataSource,
    private val imageNetworkDataSource: ImageNetworkDataSource,
    private val userDataStore: UserDataStore,
    private val authRepository: AuthRepository,
) : UserRepository {

    override val nickname: Flow<String?> = userDataStore.nickname
    override val email: Flow<String?> = userDataStore.email

    private val _myPageProfile = MutableStateFlow<MyPageProfile?>(null)
    override val myPageProfile: StateFlow<MyPageProfile?> = _myPageProfile.asStateFlow()

    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override suspend fun loadMyPage(): Result<Unit, DataError.Network> {
        if (_myPageProfile.value != null) return Result.Success(Unit)
        return refreshMyPage()
    }

    override suspend fun refreshMyPage(): Result<Unit, DataError.Network> {
        val fetchStart = System.currentTimeMillis()
        val result = userNetworkDataSource.getMyPage()
        Timber.d("[MyPageLoad] getMyPage ${System.currentTimeMillis() - fetchStart}ms")

        return result
            .map { response -> response.toMyPageProfile() }
            .onSuccess { profile ->
                _myPageProfile.value = profile
                loadProfileImageBytes(profile)
            }
            .asEmptyDataResult()
    }

    /** 프로필 이미지는 인증이 필요해서 URL을 바로 못 쓴다 - 나머지 프로필 정보를 먼저 보여주고
     *  이미지 바이트는 백그라운드에서 따로 받아와 나중에 채워 넣는다 */
    private fun loadProfileImageBytes(profile: MyPageProfile) {
        val imageId = profile.profileImageUrl?.toImageId() ?: return
        repositoryScope.launch {
            val imageStart = System.currentTimeMillis()
            val bytes = (imageNetworkDataSource.getImage(imageId) as? Result.Success)?.data
            Timber.d("[MyPageLoad] withProfileImageBytes ${System.currentTimeMillis() - imageStart}ms")

            if (bytes != null && _myPageProfile.value?.profileImageUrl == profile.profileImageUrl) {
                _myPageProfile.value = _myPageProfile.value?.copy(profileImageBytes = bytes)
            }
        }
    }

    override suspend fun getUserSetting(): Result<UserSetting, DataError.Network> {
        return userNetworkDataSource.getUserSetting()
            .map { response -> response.toUserSetting() }
            .onSuccess { setting ->
                // 서버 원본 값으로 로컬 캐시 갱신
                setting.nickname?.let { userDataStore.saveNickname(it) }
                setting.email?.let { userDataStore.saveEmail(it) }
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
    ): Result<Unit, DataError.Network> {
        return imageNetworkDataSource.updateProfileImage(imageBytes, fileName, mimeType)
            .onSuccess { refreshMyPage() }
            .asEmptyDataResult()
    }

    override suspend fun addAnimeStatus(animeId: Long, status: AnimeWatchStatus): Result<Unit, DataError.Network> {
        return userNetworkDataSource.addAnimeStatus(animeId, status)
            .onSuccess { refreshMyPage() }
    }

    override suspend fun updateAnimeStatus(animeId: Long, status: AnimeWatchStatus): Result<Unit, DataError.Network> {
        return userNetworkDataSource.updateAnimeStatus(animeId, status)
            .onSuccess { refreshMyPage() }
    }

    override suspend fun deleteAnimeStatus(animeId: Long): Result<Unit, DataError.Network> {
        return userNetworkDataSource.deleteAnimeStatus(animeId)
            .onSuccess { refreshMyPage() }
    }

    override suspend fun getMyPageAnimes(
        status: AnimeWatchStatus,
        lastId: Long?,
        size: Int,
    ): Result<CursorPage<Anime>, DataError.Network> {
        return userNetworkDataSource.getMyPageAnimes(status, lastId, size).map { response ->
            CursorPage(
                cursor = response.cursor.toCursor(),
                items = response.animes?.map { it.toAnime() },
                count = response.count,
            )
        }
    }

    override suspend fun getRatedAnimes(
        lastId: Long?,
        lastLikeCount: Long?,
        lastRating: Float?,
        size: Int,
        sort: String?,
        reviewOnly: Boolean?,
    ): Result<CursorPage<Review>, DataError.Network> {
        return userNetworkDataSource.getRatedAnimes(lastId, lastLikeCount, lastRating, size, sort, reviewOnly)
            .map { response ->
                CursorPage(
                    cursor = response.cursor.toCursor(),
                    items = response.reviews?.map { it.toReview() },
                    count = response.count,
                )
            }
    }

    override suspend fun getLikedAnimes(lastId: Long?, size: Int): Result<CursorPage<Anime>, DataError.Network> {
        return userNetworkDataSource.getLikedAnimes(lastId, size).map { response ->
            CursorPage(
                cursor = response.cursor.toCursor(),
                items = response.animes?.map { it.toAnime() },
                count = response.count,
            )
        }
    }

    override suspend fun getLikedPersons(lastId: Long?, size: Int): Result<CursorPage<Actor>, DataError.Network> {
        return userNetworkDataSource.getLikedPersons(lastId, size).map { response ->
            CursorPage(
                cursor = response.cursor.toCursor(),
                items = response.persons?.map { it.toActor() },
                count = response.count,
            )
        }
    }

    override suspend fun getMyCommunityPosts(
        lastId: Long?,
        size: Int,
    ): Result<CursorPage<CommunityPost>, DataError.Network> {
        return userNetworkDataSource.getMyCommunityPosts(lastId, size).map { response ->
            CursorPage(
                cursor = response.cursor.toCursor(),
                items = response.posts?.map { it.toCommunityPost() },
                count = response.count,
            )
        }
    }

    override suspend fun getMyCommunityComments(
        lastId: Long?,
        size: Int,
    ): Result<CursorPage<MyCommunityComment>, DataError.Network> {
        return userNetworkDataSource.getMyCommunityComments(lastId, size).map { response ->
            CursorPage(
                cursor = response.cursor.toCursor(),
                items = response.comments?.map { it.toMyCommunityComment() },
                count = response.count,
            )
        }
    }

    override suspend fun blockUser(userId: Long): Result<Unit, DataError.Network> {
        return userNetworkDataSource.blockUser(userId)
    }
}
