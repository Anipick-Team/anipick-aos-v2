package com.jparkbro.core.data.user

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.common.result.map
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.data.auth.AuthRepository
import com.jparkbro.core.datastore.UserDataStore
import com.jparkbro.core.model.anime.AnimeWatchStatus
import com.jparkbro.core.model.mypage.MyPageAnimesResult
import com.jparkbro.core.model.mypage.MyPageCommunityCommentsResult
import com.jparkbro.core.model.mypage.MyPageCommunityPostsResult
import com.jparkbro.core.model.mypage.MyPageLikedAnimesResult
import com.jparkbro.core.model.mypage.MyPageLikedPersonsResult
import com.jparkbro.core.model.mypage.MyPageProfile
import com.jparkbro.core.model.mypage.MyPageRatedAnimesResult
import com.jparkbro.core.model.user.UserSetting
import com.jparkbro.core.network.common.toCursor
import com.jparkbro.core.network.image.ImageNetworkDataSource
import com.jparkbro.core.network.user.UserNetworkDataSource
import com.jparkbro.core.network.user.dto.toActor
import com.jparkbro.core.network.user.dto.toAnime
import com.jparkbro.core.network.user.dto.toCommunityPost
import com.jparkbro.core.network.user.dto.toMyCommunityComment
import com.jparkbro.core.network.user.dto.toMyPageProfile
import com.jparkbro.core.network.user.dto.toReview
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
    ): Result<Long, DataError.Network> {
        return imageNetworkDataSource.updateProfileImage(imageBytes, fileName, mimeType)
            .map { it.imageId }
    }

    override suspend fun addAnimeStatus(animeId: Long, status: AnimeWatchStatus): Result<Unit, DataError.Network> {
        return userNetworkDataSource.addAnimeStatus(animeId, status)
    }

    override suspend fun updateAnimeStatus(animeId: Long, status: AnimeWatchStatus): Result<Unit, DataError.Network> {
        return userNetworkDataSource.updateAnimeStatus(animeId, status)
    }

    override suspend fun deleteAnimeStatus(animeId: Long): Result<Unit, DataError.Network> {
        return userNetworkDataSource.deleteAnimeStatus(animeId)
    }

    override suspend fun getMyPageAnimes(
        status: AnimeWatchStatus,
        lastId: Long?,
        size: Int,
    ): Result<MyPageAnimesResult, DataError.Network> {
        return userNetworkDataSource.getMyPageAnimes(status, lastId, size).map { response ->
            MyPageAnimesResult(
                count = response.count,
                cursor = response.cursor.toCursor(),
                animes = response.animes?.map { it.toAnime() },
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
    ): Result<MyPageRatedAnimesResult, DataError.Network> {
        return userNetworkDataSource.getRatedAnimes(lastId, lastLikeCount, lastRating, size, sort, reviewOnly)
            .map { response ->
                MyPageRatedAnimesResult(
                    count = response.count,
                    cursor = response.cursor.toCursor(),
                    reviews = response.reviews?.map { it.toReview() },
                )
            }
    }

    override suspend fun getLikedAnimes(lastId: Long?, size: Int): Result<MyPageLikedAnimesResult, DataError.Network> {
        return userNetworkDataSource.getLikedAnimes(lastId, size).map { response ->
            MyPageLikedAnimesResult(
                count = response.count,
                cursor = response.cursor.toCursor(),
                animes = response.animes?.map { it.toAnime() },
            )
        }
    }

    override suspend fun getLikedPersons(lastId: Long?, size: Int): Result<MyPageLikedPersonsResult, DataError.Network> {
        return userNetworkDataSource.getLikedPersons(lastId, size).map { response ->
            MyPageLikedPersonsResult(
                count = response.count,
                cursor = response.cursor.toCursor(),
                persons = response.persons?.map { it.toActor() },
            )
        }
    }

    override suspend fun getMyCommunityPosts(
        lastId: Long?,
        size: Int,
    ): Result<MyPageCommunityPostsResult, DataError.Network> {
        return userNetworkDataSource.getMyCommunityPosts(lastId, size).map { response ->
            MyPageCommunityPostsResult(
                count = response.count,
                cursor = response.cursor.toCursor(),
                posts = response.posts?.map { it.toCommunityPost() },
            )
        }
    }

    override suspend fun getMyCommunityComments(
        lastId: Long?,
        size: Int,
    ): Result<MyPageCommunityCommentsResult, DataError.Network> {
        return userNetworkDataSource.getMyCommunityComments(lastId, size).map { response ->
            MyPageCommunityCommentsResult(
                count = response.count,
                cursor = response.cursor.toCursor(),
                comments = response.comments?.map { it.toMyCommunityComment() },
            )
        }
    }
}
