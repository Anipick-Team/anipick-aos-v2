package com.jparkbro.core.network.anime

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.network.anime.dto.AnimeDetailResponse
import com.jparkbro.core.network.anime.dto.AnimeSummaryResponse
import com.jparkbro.core.network.anime.dto.ComingSoonAnimesDetailResponse
import com.jparkbro.core.network.anime.dto.ComingSoonAnimesRequest
import com.jparkbro.core.network.anime.dto.PreferenceSetupSearchRequest
import com.jparkbro.core.network.anime.dto.PreferenceSetupSearchResponse
import com.jparkbro.core.network.anime.dto.UpcomingSeasonAnimesResponse
import com.jparkbro.core.network.character.dto.AnimeCharacterResponse
import com.jparkbro.core.network.series.dto.SeriesAnimeResponse

interface AnimeNetworkDataSource {
    /** 선호 애니 설정 검색 - `GET /explore-search`. */
    suspend fun searchPreferenceSetupAnimes(
        request: PreferenceSetupSearchRequest,
    ): Result<PreferenceSetupSearchResponse, DataError.Network>

    /** 홈 방영예정 섹션 - `GET /animes/upcoming-season`. */
    suspend fun getUpcomingSeasonAnimes(): Result<UpcomingSeasonAnimesResponse, DataError.Network>

    /** 홈 방영예정 더보기 - `GET /animes/coming-soon`. */
    suspend fun getComingSoonAnimesDetail(
        request: ComingSoonAnimesRequest,
    ): Result<ComingSoonAnimesDetailResponse, DataError.Network>

    /** 애니 상세 "정보" 탭 - `GET /animes/{animeId}/detail/info`. */
    suspend fun getAnimeDetailInfo(animeId: Long): Result<AnimeDetailResponse, DataError.Network>

    /** 애니 상세 "정보" 탭의 출연진 미리보기 - `GET /animes/{animeId}/detail/actor`. */
    suspend fun getAnimeDetailCast(animeId: Long): Result<List<AnimeCharacterResponse>, DataError.Network>

    /** 애니 상세 "정보" 탭의 시리즈 미리보기 - `GET /animes/{animeId}/detail/series`. */
    suspend fun getAnimeDetailSeries(animeId: Long): Result<List<SeriesAnimeResponse>, DataError.Network>

    /** 애니 상세 "정보" 탭의 추천 작품 미리보기 - `GET /animes/{animeId}/detail/recommendation`. */
    suspend fun getAnimeDetailRecommendations(animeId: Long): Result<List<AnimeSummaryResponse>, DataError.Network>

    /** 애니 찜하기 - `POST /animes/{animeId}/like`. */
    suspend fun likeAnime(animeId: Long): Result<Unit, DataError.Network>

    /** 애니 찜 취소 - `DELETE /animes/{animeId}/like`. */
    suspend fun unlikeAnime(animeId: Long): Result<Unit, DataError.Network>
}
