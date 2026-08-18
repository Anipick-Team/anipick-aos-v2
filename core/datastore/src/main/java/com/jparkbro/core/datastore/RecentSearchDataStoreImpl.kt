package com.jparkbro.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.recentSearchDataStore: DataStore<Preferences> by preferencesDataStore(name = "recent_search_preferences")

private object RecentSearchKeys {
    val RECENT_SEARCHES = stringPreferencesKey("recent_searches")
}

/** 검색어에 나올 일이 없는 개행 문자로 목록을 이어붙여 하나의 문자열 prefs 값에 저장한다. */
private const val RECENT_SEARCH_DELIMITER = "\n"
private const val MAX_RECENT_SEARCHES = 10

class RecentSearchDataStoreImpl(
    private val context: Context,
) : RecentSearchDataStore {

    override val recentSearches: Flow<List<String>> =
        context.recentSearchDataStore.data.map { it.toRecentSearches() }

    override suspend fun getRecentSearches(): List<String> = recentSearches.first()

    /** 이미 있던 검색어면 지우고 맨 앞에 다시 추가해서 최신순을 유지하고, [MAX_RECENT_SEARCHES]개를 넘으면 오래된 것부터 지운다. */
    override suspend fun saveRecentSearch(query: String) {
        context.recentSearchDataStore.edit { prefs ->
            val updated = listOf(query) + prefs.toRecentSearches().filterNot { it == query }
            prefs[RecentSearchKeys.RECENT_SEARCHES] = updated.take(MAX_RECENT_SEARCHES).joinToString(RECENT_SEARCH_DELIMITER)
        }
    }

    override suspend fun removeRecentSearch(query: String) {
        context.recentSearchDataStore.edit { prefs ->
            val updated = prefs.toRecentSearches().filterNot { it == query }
            prefs[RecentSearchKeys.RECENT_SEARCHES] = updated.joinToString(RECENT_SEARCH_DELIMITER)
        }
    }

    override suspend fun clearRecentSearches() {
        context.recentSearchDataStore.edit { it.clear() }
    }

    private fun Preferences.toRecentSearches(): List<String> =
        this[RecentSearchKeys.RECENT_SEARCHES]
            ?.split(RECENT_SEARCH_DELIMITER)
            ?.filter { it.isNotEmpty() }
            ?: emptyList()
}
