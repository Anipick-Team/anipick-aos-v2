package com.jparkbro.core.network.user

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.AnimeWatchStatus
import com.jparkbro.core.network.user.dto.LikedAnimesResponse
import com.jparkbro.core.network.user.dto.LikedPersonsResponse
import com.jparkbro.core.network.user.dto.MyPageAnimesResponse
import com.jparkbro.core.network.user.dto.MyPageCommunityCommentsResponse
import com.jparkbro.core.network.user.dto.MyPageCommunityPostsResponse
import com.jparkbro.core.network.user.dto.MyPageResponse
import com.jparkbro.core.network.user.dto.RatedAnimesResponse
import com.jparkbro.core.network.user.dto.UserSettingResponse

interface UserNetworkDataSource {
    /** 마이페이지 - `GET /mypage`. */
    suspend fun getMyPage(): Result<MyPageResponse, DataError.Network>

    /** 설정 화면 - `GET /setting/view`. */
    suspend fun getUserSetting(): Result<UserSettingResponse, DataError.Network>

    /** 설정 - 닉네임 변경 - `PATCH /setting/nickname`. */
    suspend fun updateNickname(nickname: String): Result<Unit, DataError.Network>

    /** 설정 - 이메일 변경 - `PUT /setting/email`. */
    suspend fun updateEmail(newEmail: String, password: String): Result<Unit, DataError.Network>

    /** 설정 - 비밀번호 변경 - `PATCH /setting/password`. */
    suspend fun updatePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
    ): Result<Unit, DataError.Network>

    /** 설정 - 회원 탈퇴 - `PATCH /setting/withdrawal`. */
    suspend fun withdraw(): Result<Unit, DataError.Network>

    /** 애니 보기 상태 등록 - `POST /users/{animeId}/status`. */
    suspend fun addAnimeStatus(animeId: Long, status: AnimeWatchStatus): Result<Unit, DataError.Network>

    /** 애니 보기 상태 수정 - `PATCH /users/{animeId}/status`. */
    suspend fun updateAnimeStatus(animeId: Long, status: AnimeWatchStatus): Result<Unit, DataError.Network>

    /** 애니 보기 상태 삭제 - `DELETE /users/{animeId}/status`. */
    suspend fun deleteAnimeStatus(animeId: Long): Result<Unit, DataError.Network>

    /** 마이페이지 보기 상태별 애니 목록 - `GET /mypage/animes/{watchlist|watching|finished}`. */
    suspend fun getMyPageAnimes(
        status: AnimeWatchStatus,
        lastId: Long? = null,
        size: Int = 18,
    ): Result<MyPageAnimesResponse, DataError.Network>

    /** 마이페이지 "평가한 애니" 목록 - `GET /mypage/animes/rated`. */
    suspend fun getRatedAnimes(
        lastId: Long? = null,
        lastLikeCount: Long? = null,
        lastRating: Float? = null,
        size: Int = 20,
        sort: String? = null,
        reviewOnly: Boolean? = null,
    ): Result<RatedAnimesResponse, DataError.Network>

    /** 마이페이지 "찜한 애니" 목록 - `GET /mypage/animes/like`. */
    suspend fun getLikedAnimes(lastId: Long? = null, size: Int = 18): Result<LikedAnimesResponse, DataError.Network>

    /** 마이페이지 "찜한 인물" 목록 - `GET /mypage/persons/like`. */
    suspend fun getLikedPersons(lastId: Long? = null, size: Int = 18): Result<LikedPersonsResponse, DataError.Network>

    /** 마이페이지 "내가 쓴 게시글" 목록 - `GET /mypage/community/posts`. */
    suspend fun getMyCommunityPosts(
        lastId: Long? = null,
        size: Int = 20,
    ): Result<MyPageCommunityPostsResponse, DataError.Network>

    /** 마이페이지 "내가 쓴 댓글" 목록 - `GET /mypage/community/comments`. */
    suspend fun getMyCommunityComments(
        lastId: Long? = null,
        size: Int = 20,
    ): Result<MyPageCommunityCommentsResponse, DataError.Network>
}
