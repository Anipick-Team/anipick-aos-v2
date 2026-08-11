package com.jparkbro.core.data.anime

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.anime.PreferenceSetupSearchResult
import com.jparkbro.core.model.anime.RecommendationResult
import com.jparkbro.core.model.anime.UpcomingSeasonResult
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    /** 마지막으로 애니 상세에 진입한 animeId. 한 곳(datastore)만 바라보는 값이라, 어디서 바뀌든
     *  이 Flow를 구독하는 화면에 그대로 반영된다. */
    val recentAnimeId: Flow<Long?>

    suspend fun searchPreferenceSetupAnimes(
        query: String? = null,
        year: String? = null,
        season: Int? = null,
        genres: Int? = null,
        lastId: Long? = null,
        size: Int? = 10,
    ): Result<PreferenceSetupSearchResult, DataError.Network>
    suspend fun getTrendingAnimes(): Result<List<Anime>, DataError.Network>
    suspend fun getRecommendationAnimes(): Result<RecommendationResult, DataError.Network>
    suspend fun getRecentAnimeRecommendations(animeId: Long): Result<RecommendationResult, DataError.Network>
    suspend fun getUpcomingSeasonAnimes(): Result<UpcomingSeasonResult, DataError.Network>
    suspend fun getComingSoonAnimes(): Result<List<Anime>, DataError.Network>
}
