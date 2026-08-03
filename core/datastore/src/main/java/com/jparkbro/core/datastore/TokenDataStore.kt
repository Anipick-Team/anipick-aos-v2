package com.jparkbro.core.datastore

import android.content.Context
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jparkbro.core.common.auth.TokenProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONObject

private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = "token_preferences")

private object Keys {
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
}

class TokenDataStore(
    private val context: Context,
) : TokenProvider {
    val accessToken: Flow<String?> = context.tokenDataStore.data.map { it[Keys.ACCESS_TOKEN] }
    val refreshToken: Flow<String?> = context.tokenDataStore.data.map { it[Keys.REFRESH_TOKEN] }

    override val isLoggedIn: Flow<Boolean> = accessToken.map { it != null }

    override suspend fun getAccessToken(): String? = accessToken.first()

    override suspend fun getRefreshToken(): String? = refreshToken.first()

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.tokenDataStore.edit { prefs ->
            prefs[Keys.ACCESS_TOKEN] = accessToken
            prefs[Keys.REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun clearTokens() {
        context.tokenDataStore.edit { it.clear() }
    }

    override suspend fun isRefreshTokenValid(): Boolean {
        val token = getRefreshToken() ?: return false
        return !isJwtExpired(token)
    }

    private fun isJwtExpired(jwt: String): Boolean {
        val decoded = decodeJwt(jwt) ?: return true
        val expiresAtSeconds = decoded.optLong("exp", 0L)
        if (expiresAtSeconds == 0L) return true

        val nowSeconds = System.currentTimeMillis() / 1000
        return nowSeconds > expiresAtSeconds
    }

    private fun decodeJwt(jwt: String): JSONObject? {
        return try {
            val payload = jwt.split(".")[1]
            val decoded = Base64.decode(payload, Base64.URL_SAFE)
            JSONObject(String(decoded))
        } catch (e: Exception) {
            null
        }
    }
}
