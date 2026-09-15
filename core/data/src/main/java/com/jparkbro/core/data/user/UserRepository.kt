package com.jparkbro.core.data.user

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.actor.Actor
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.anime.AnimeWatchStatus
import com.jparkbro.core.model.community.CommunityPost
import com.jparkbro.core.model.mypage.MyCommunityComment
import com.jparkbro.core.model.mypage.MyPageProfile
import com.jparkbro.core.model.pagination.CursorPage
import com.jparkbro.core.model.review.Review
import com.jparkbro.core.model.user.UserSetting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/** 유저 정보 관련 데이터를 읽고 쓰는 인터페이스 */
interface UserRepository {

    /** 닉네임 */
    val nickname: Flow<String?>
    /** 이메일 */
    val email: Flow<String?>

    /** 캐시된 마이페이지 프로필 - 아직 조회 안 됐으면 null */
    val myPageProfile: StateFlow<MyPageProfile?>

    /** 캐시 있으면 유지, 없으면 `GET /mypage`로 조회 */
    suspend fun loadMyPage(): Result<Unit, DataError.Network>

    /** 캐시 무시하고 `GET /mypage`로 재조회해 [myPageProfile] 갱신 */
    suspend fun refreshMyPage(): Result<Unit, DataError.Network>

    /** 설정 화면 - `GET /setting/view`. */
    suspend fun getUserSetting(): Result<UserSetting, DataError.Network>

    /** 닉네임 변경 - `PATCH /setting/nickname`. */
    suspend fun updateNickname(nickname: String): Result<Unit, DataError.Network>

    /** 이메일 변경 - `PUT /setting/email`. */
    suspend fun updateEmail(newEmail: String, password: String): Result<Unit, DataError.Network>

    /** 비밀번호 변경 - `PATCH /setting/password`. */
    suspend fun updatePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
    ): Result<Unit, DataError.Network>

    /** 회원 탈퇴 - `PATCH /setting/withdrawal`. */
    suspend fun withdraw(): Result<Unit, DataError.Network>

    /** 프로필 이미지 변경 - `POST /image/profile-image`, 성공하면 [myPageProfile]도 새 이미지로 갱신 */
    suspend fun updateProfileImage(
        imageBytes: ByteArray,
        fileName: String,
        mimeType: String,
    ): Result<Unit, DataError.Network>

    /** 애니 보기 상태 등록 - `POST /users/{animeId}/status`. 성공하면 [refreshMyPage]로 [myPageProfile]을 갱신한다. */
    suspend fun addAnimeStatus(animeId: Long, status: AnimeWatchStatus): Result<Unit, DataError.Network>

    /** 애니 보기 상태 수정 - `PATCH /users/{animeId}/status`. 성공하면 [refreshMyPage]로 [myPageProfile]을 갱신한다. */
    suspend fun updateAnimeStatus(animeId: Long, status: AnimeWatchStatus): Result<Unit, DataError.Network>

    /** 애니 보기 상태 삭제 - `DELETE /users/{animeId}/status`. 성공하면 [refreshMyPage]로 [myPageProfile]을 갱신한다. */
    suspend fun deleteAnimeStatus(animeId: Long): Result<Unit, DataError.Network>

    /** 마이페이지 보기 상태별 애니 목록 - `GET /mypage/animes/{watchlist|watching|finished}`. */
    suspend fun getMyPageAnimes(
        status: AnimeWatchStatus,
        lastId: Long? = null,
        size: Int = 18,
    ): Result<CursorPage<Anime>, DataError.Network>

    /** 마이페이지 "평가한 애니" 목록 - `GET /mypage/animes/rated`. */
    suspend fun getRatedAnimes(
        lastId: Long? = null,
        lastLikeCount: Long? = null,
        lastRating: Float? = null,
        size: Int = 20,
        sort: String? = null,
        reviewOnly: Boolean? = null,
    ): Result<CursorPage<Review>, DataError.Network>

    /** 마이페이지 "찜한 애니" 목록 - `GET /mypage/animes/like`. */
    suspend fun getLikedAnimes(lastId: Long? = null, size: Int = 18): Result<CursorPage<Anime>, DataError.Network>

    /** 마이페이지 "찜한 인물" 목록 - `GET /mypage/persons/like`. */
    suspend fun getLikedPersons(lastId: Long? = null, size: Int = 18): Result<CursorPage<Actor>, DataError.Network>

    /** 마이페이지 "내가 쓴 게시글" 목록 - `GET /mypage/community/posts`. */
    suspend fun getMyCommunityPosts(
        lastId: Long? = null,
        size: Int = 20,
    ): Result<CursorPage<CommunityPost>, DataError.Network>

    /** 마이페이지 "내가 쓴 댓글" 목록 - `GET /mypage/community/comments`. */
    suspend fun getMyCommunityComments(
        lastId: Long? = null,
        size: Int = 20,
    ): Result<CursorPage<MyCommunityComment>, DataError.Network>

    /** 유저 차단 - `POST /users/{userId}/block`. */
    suspend fun blockUser(userId: Long): Result<Unit, DataError.Network>
}
