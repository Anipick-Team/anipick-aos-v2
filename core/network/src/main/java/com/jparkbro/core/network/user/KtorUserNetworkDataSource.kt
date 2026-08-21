package com.jparkbro.core.network.user

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.AnimeWatchStatus
import com.jparkbro.core.network.delete
import com.jparkbro.core.network.get
import com.jparkbro.core.network.patch
import com.jparkbro.core.network.post
import com.jparkbro.core.network.put
import com.jparkbro.core.network.user.dto.EmailUpdateRequest
import com.jparkbro.core.network.user.dto.LikedAnimesResponse
import com.jparkbro.core.network.user.dto.LikedPersonsResponse
import com.jparkbro.core.network.user.dto.MyPageAnimesResponse
import com.jparkbro.core.network.user.dto.MyPageCommunityCommentsResponse
import com.jparkbro.core.network.user.dto.MyPageCommunityPostsResponse
import com.jparkbro.core.network.user.dto.MyPageResponse
import com.jparkbro.core.network.user.dto.NicknameUpdateRequest
import com.jparkbro.core.network.user.dto.PasswordUpdateRequest
import com.jparkbro.core.network.user.dto.RatedAnimesResponse
import com.jparkbro.core.network.user.dto.UserAnimeStatusRequest
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

    override suspend fun addAnimeStatus(animeId: Long, status: AnimeWatchStatus): Result<Unit, DataError.Network> {
        return httpClient.post(
            route = "/users/$animeId/status",
            body = UserAnimeStatusRequest(status = status.name),
        )
    }

    override suspend fun updateAnimeStatus(animeId: Long, status: AnimeWatchStatus): Result<Unit, DataError.Network> {
        return httpClient.patch(
            route = "/users/$animeId/status",
            body = UserAnimeStatusRequest(status = status.name),
        )
    }

    override suspend fun deleteAnimeStatus(animeId: Long): Result<Unit, DataError.Network> {
        return httpClient.delete(route = "/users/$animeId/status")
    }

    override suspend fun getMyPageAnimes(
        status: AnimeWatchStatus,
        lastId: Long?,
        size: Int,
    ): Result<MyPageAnimesResponse, DataError.Network> {
        return httpClient.get(
            route = "/mypage/animes/${status.name.lowercase()}",
            queryParameters = mapOf(
                "status" to status.name,
                "lastId" to lastId,
                "size" to size,
            ),
        )
    }

    override suspend fun getRatedAnimes(
        lastId: Long?,
        lastLikeCount: Long?,
        lastRating: Float?,
        size: Int,
        sort: String?,
        reviewOnly: Boolean?,
    ): Result<RatedAnimesResponse, DataError.Network> {
        return httpClient.get(
            route = "/mypage/animes/rated",
            queryParameters = mapOf(
                "lastId" to lastId,
                "lastLikeCount" to lastLikeCount,
                "lastRating" to lastRating,
                "size" to size,
                "sort" to sort,
                "reviewOnly" to reviewOnly,
            ),
        )
    }

    override suspend fun getLikedAnimes(lastId: Long?, size: Int): Result<LikedAnimesResponse, DataError.Network> {
        return httpClient.get(
            route = "/mypage/animes/like",
            queryParameters = mapOf(
                "lastId" to lastId,
                "size" to size,
            ),
        )
    }

    override suspend fun getLikedPersons(lastId: Long?, size: Int): Result<LikedPersonsResponse, DataError.Network> {
        return httpClient.get(
            route = "/mypage/persons/like",
            queryParameters = mapOf(
                "lastId" to lastId,
                "size" to size,
            ),
        )
    }

    override suspend fun getMyCommunityPosts(
        lastId: Long?,
        size: Int,
    ): Result<MyPageCommunityPostsResponse, DataError.Network> {
        return httpClient.get(
            route = "/mypage/community/posts",
            queryParameters = mapOf(
                "lastId" to lastId,
                "size" to size,
            ),
        )
    }

    override suspend fun getMyCommunityComments(
        lastId: Long?,
        size: Int,
    ): Result<MyPageCommunityCommentsResponse, DataError.Network> {
        return httpClient.get(
            route = "/mypage/community/comments",
            queryParameters = mapOf(
                "lastId" to lastId,
                "size" to size,
            ),
        )
    }
}
