package com.jparkbro.core.data.search

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.search.SearchActorResult
import com.jparkbro.core.model.search.SearchAnimeResult
import com.jparkbro.core.model.search.SearchStudioResult
import kotlinx.coroutines.flow.Flow

/** 검색 관련 데이터를 읽고 쓰는 인터페이스 */
interface SearchRepository {
    /** 검색 화면 진입 시 인기 애니 - `GET search/init`. */
    suspend fun getPopularAnimes(): Result<List<Anime>?, DataError.Network>

    /** 애니 검색 결과 - `GET search/animes`. */
    suspend fun getSearchAnimes(
        query: String,
        lastId: Long? = null,
        size: Int = 18,
        page: Long = 1,
    ): Result<SearchAnimeResult, DataError.Network>

    /** 배우 검색 결과 - `GET search/persons`. */
    suspend fun getSearchActors(
        query: String,
        lastId: Long? = null,
        size: Int = 18,
    ): Result<SearchActorResult, DataError.Network>

    /** 스튜디오 검색 결과 - `GET search/studios`. */
    suspend fun getSearchStudios(
        query: String,
        lastId: Long? = null,
        size: Int = 18,
    ): Result<SearchStudioResult, DataError.Network>

    /** 최근 검색어 목록 */
    val recentSearches: Flow<List<String>>

    suspend fun getRecentSearches(): List<String>
    suspend fun saveRecentSearch(query: String)
    suspend fun removeRecentSearch(query: String)
    suspend fun clearRecentSearches()
}
