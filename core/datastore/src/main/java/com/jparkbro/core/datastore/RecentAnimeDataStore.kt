package com.jparkbro.core.datastore

import kotlinx.coroutines.flow.Flow

/** 애니 상세 화면에 마지막으로 진입한 [animeId][Long]를 저장한다. */
interface RecentAnimeDataStore {
    val recentAnimeId: Flow<Long?>

    suspend fun getRecentAnimeId(): Long?
    suspend fun saveRecentAnimeId(animeId: Long)
    suspend fun clearRecentAnimeId()
}
