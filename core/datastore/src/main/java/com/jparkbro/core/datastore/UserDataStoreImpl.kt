package com.jparkbro.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

private object UserKeys {
    val USER_ID = longPreferencesKey("user_id")
    val NICKNAME = stringPreferencesKey("nickname")
    val EMAIL = stringPreferencesKey("email")
}

class UserDataStoreImpl(
    private val context: Context,
) : UserDataStore {
    override val userId: Flow<Long?> = context.userDataStore.data.map { it[UserKeys.USER_ID] }
    override val nickname: Flow<String?> = context.userDataStore.data.map { it[UserKeys.NICKNAME] }
    override val email: Flow<String?> = context.userDataStore.data.map { it[UserKeys.EMAIL] }

    override suspend fun getUserId(): Long? = userId.first()

    override suspend fun getNickname(): String? = nickname.first()

    override suspend fun getEmail(): String? = email.first()

    override suspend fun saveUser(userId: Long, nickname: String) {
        context.userDataStore.edit { prefs ->
            prefs[UserKeys.USER_ID] = userId
            prefs[UserKeys.NICKNAME] = nickname
        }
    }

    override suspend fun saveNickname(nickname: String) {
        context.userDataStore.edit { prefs -> prefs[UserKeys.NICKNAME] = nickname }
    }

    override suspend fun saveEmail(email: String) {
        context.userDataStore.edit { prefs -> prefs[UserKeys.EMAIL] = email }
    }

    override suspend fun clearUser() {
        context.userDataStore.edit { it.clear() }
    }
}
