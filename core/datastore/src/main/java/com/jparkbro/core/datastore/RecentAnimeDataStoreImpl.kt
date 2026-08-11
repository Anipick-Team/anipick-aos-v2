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

class RecentAnimeDataStoreImpl(
    private val context: Context,
) : RecentAnimeDataStore {
    override val recentAnimeId: Flow<Long?> =
        context.recentAnimeDataStore.data.map { it[RecentAnimeKeys.RECENT_ANIME_ID] }

    override suspend fun getRecentAnimeId(): Long? = recentAnimeId.first()

    override suspend fun saveRecentAnimeId(animeId: Long) {
        context.recentAnimeDataStore.edit { prefs ->
            prefs[RecentAnimeKeys.RECENT_ANIME_ID] = animeId
        }
    }

    override suspend fun clearRecentAnimeId() {
        context.recentAnimeDataStore.edit { it.clear() }
    }
}
