package com.jparkbro.core.datastore

import kotlinx.coroutines.flow.Flow

/** 로그인한 사용자 정보를 저장하고 읽는 인터페이스 */
interface UserDataStore {
    val userId: Flow<Long?>
    val nickname: Flow<String?>
    val email: Flow<String?>

    suspend fun getUserId(): Long?
    suspend fun getNickname(): String?
    suspend fun getEmail(): String?
    suspend fun saveUser(userId: Long, nickname: String)
    suspend fun saveNickname(nickname: String)
    suspend fun saveEmail(email: String)
    suspend fun clearUser()
}
