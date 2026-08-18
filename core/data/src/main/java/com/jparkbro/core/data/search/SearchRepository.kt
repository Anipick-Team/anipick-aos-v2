package com.jparkbro.core.data.search

import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.Result
import com.jparkbro.core.model.anime.Anime
import com.jparkbro.core.model.search.SearchActorResult
import com.jparkbro.core.model.search.SearchAnimeResult
import com.jparkbro.core.model.search.SearchStudioResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun getPopularAnimes(): Result<List<Anime>, DataError.Network>

    suspend fun getSearchAnimes(
        query: String,
        lastId: Long? = null,
        size: Int = 18,
        page: Long = 1,
    ): Result<SearchAnimeResult, DataError.Network>

    suspend fun getSearchActors(
        query: String,
        lastId: Long? = null,
        size: Int = 18,
    ): Result<SearchActorResult, DataError.Network>

    suspend fun getSearchStudios(
        query: String,
        lastId: Long? = null,
        size: Int = 18,
    ): Result<SearchStudioResult, DataError.Network>

    val recentSearches: Flow<List<String>>

    suspend fun getRecentSearches(): List<String>
    suspend fun saveRecentSearch(query: String)
    suspend fun removeRecentSearch(query: String)
    suspend fun clearRecentSearches()
}
