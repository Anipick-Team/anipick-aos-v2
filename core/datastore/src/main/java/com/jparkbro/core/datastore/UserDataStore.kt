package com.jparkbro.core.datastore

import kotlinx.coroutines.flow.Flow

/** 로그인 응답(OAuth 등)에서 받은 [userId]/[nickname]을 저장한다. */
interface UserDataStore {
    val userId: Flow<Long?>
    val nickname: Flow<String?>

    suspend fun getUserId(): Long?
    suspend fun getNickname(): String?
    suspend fun saveUser(userId: Long, nickname: String)
    suspend fun clearUser()
}
