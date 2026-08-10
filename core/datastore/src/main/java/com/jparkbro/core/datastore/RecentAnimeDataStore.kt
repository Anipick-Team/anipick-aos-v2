package com.jparkbro.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.recentAnimeDataStore: DataStore<Preferences> by preferencesDataStore(name = "recent_anime_preferences")

private object RecentAnimeKeys {
    val RECENT_ANIME_ID = longPreferencesKey("recent_anime_id")
}

/** 애니 상세 화면에 마지막으로 진입한 [animeId][Long]를 저장한다. */
class RecentAnimeDataStore(
    private val context: Context,
) {
    val recentAnimeId: Flow<Long?> = context.recentAnimeDataStore.data.map { it[RecentAnimeKeys.RECENT_ANIME_ID] }

    suspend fun getRecentAnimeId(): Long? = recentAnimeId.first()

    suspend fun saveRecentAnimeId(animeId: Long) {
        context.recentAnimeDataStore.edit { prefs ->
            prefs[RecentAnimeKeys.RECENT_ANIME_ID] = animeId
        }
    }

    suspend fun clearRecentAnimeId() {
        context.recentAnimeDataStore.edit { it.clear() }
    }
}
