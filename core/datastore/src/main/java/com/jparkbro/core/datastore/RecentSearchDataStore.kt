package com.jparkbro.core.datastore

import kotlinx.coroutines.flow.Flow

/** 최근 검색어 목록을 최신순으로 저장한다. */
interface RecentSearchDataStore {
    val recentSearches: Flow<List<String>>

    suspend fun getRecentSearches(): List<String>
    suspend fun saveRecentSearch(query: String)
    suspend fun removeRecentSearch(query: String)
    suspend fun clearRecentSearches()
}
