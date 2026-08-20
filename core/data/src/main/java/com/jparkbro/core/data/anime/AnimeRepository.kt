package com.jparkbro.core.data.anime

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.anime.AnimeDetail
import com.jparkbro.core.model.anime.ComingSoonResult
import com.jparkbro.core.model.anime.PreferenceSetupSearchResult
import com.jparkbro.core.model.anime.UpcomingSeasonResult
import com.jparkbro.core.model.character.AnimeCharacter
import kotlinx.coroutines.flow.Flow

/** 애니 관련 데이터를 읽어오는 인터페이스 */
interface AnimeRepository {

    /** 최근 조회한 애니 ID */
    val recentAnimeId: Flow<Long?>

    /** 선호작 설정 애니 검색 - `GET /explore-search`. */
    suspend fun searchPreferenceSetupAnimes(
        query: String? = null,
        year: String? = null,
        season: Int? = null,
        genres: Int? = null,
        lastId: Long? = null,
        size: Int? = 10,
    ): Result<PreferenceSetupSearchResult, DataError.Network>

    /** 홈 화면 방영 예정 시즌 애니 - `GET /animes/upcoming-season`. */
    suspend fun getUpcomingSeasonAnimes(): Result<UpcomingSeasonResult, DataError.Network>

    /** 홈 상세 "방영 예정" 목록 - `GET /animes/coming-soon`. */
    suspend fun getComingSoonAnimesDetail(
        sort: String? = null,
        lastId: Long? = null,
        lastValue: String? = null,
        size: Long = 18,
    ): Result<ComingSoonResult, DataError.Network>

    /** 애니 상세 "정보" 탭 - `GET /animes/{animeId}/detail/info`. */
    suspend fun getAnimeDetailInfo(animeId: Long): Result<AnimeDetail, DataError.Network>

    /** 애니 상세 "정보" 탭의 출연진 미리보기 - `GET /animes/{animeId}/detail/actor`. */
    suspend fun getAnimeDetailCast(animeId: Long): Result<List<AnimeCharacter>, DataError.Network>

    /** 애니 상세 "정보" 탭의 시리즈 미리보기 - `GET /animes/{animeId}/detail/series`. */
    suspend fun getAnimeDetailSeries(animeId: Long): Result<List<Anime>, DataError.Network>

    /** 애니 상세 "정보" 탭의 추천 작품 미리보기 - `GET /animes/{animeId}/detail/recommendation`. */
    suspend fun getAnimeDetailRecommendations(animeId: Long): Result<List<Anime>, DataError.Network>
}
