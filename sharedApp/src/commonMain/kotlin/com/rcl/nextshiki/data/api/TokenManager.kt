package com.rcl.nextshiki.data.api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rcl.nextshiki.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.Url
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class TokenManager (
    private val dataStore: DataStore<Preferences>,
    private val unauthenticatedHttpClient: HttpClient,
    private val clientId: String = BuildKonfig.clientId?: "",
    private val clientSecret: String = BuildKonfig.clientSecret?: "",
    private val redirectUri: String = BuildKonfig.redirectURI?: "",
    private val applicationName: String = BuildKonfig.appName?: ""
) {
    val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    val ACCESS_TOKEN_EXPIRATION_KEY = longPreferencesKey("access_token_expiration_millis")

    suspend fun getAccessToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }.first()
    }

    suspend fun getRefreshToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY]
        }.first()
    }

    private suspend fun getAccessTokenExpirationMillis(): Long? {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_EXPIRATION_KEY]
        }.first()
    }

    @OptIn(ExperimentalTime::class)
    suspend fun saveTokens(accessToken: String, refreshToken: String, expiresInSeconds: Long) {
        val expiresAtMillis = Clock.System.now().toEpochMilliseconds() + expiresInSeconds * 1000 - (60 * 1000)
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[ACCESS_TOKEN_EXPIRATION_KEY] = expiresAtMillis
        }
    }

    suspend fun isUserLoggedIn(): Boolean {
        return getAccessToken() != null && getRefreshToken() != null
    }

    @OptIn(ExperimentalTime::class)
    suspend fun isAccessTokenCurrentlyValid(): Boolean {
        val accessToken = getAccessToken()
        val expirationMillis = getAccessTokenExpirationMillis()

        if (accessToken == null || expirationMillis == null) {
            return false
        }

        return Clock.System.now().toEpochMilliseconds() < expirationMillis
    }

    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
            preferences.remove(ACCESS_TOKEN_EXPIRATION_KEY)
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun isAccessTokenValid(): Boolean {
        val accessToken = getAccessToken()
        val expirationMillis = getAccessTokenExpirationMillis()

        if (accessToken == null || expirationMillis == null) {
            return false
        }

        return Clock.System.now().toEpochMilliseconds() < expirationMillis
    }

    suspend fun exchangeCodeForTokens(authCode: String): AccessTokenResponse? {
        return try {
            val response = unauthenticatedHttpClient.post(Url("https://shikimori.one/oauth/token")) {
                headers {
                    append(HttpHeaders.UserAgent, applicationName)
                }
                setBody(formData {
                    append("grant_type", "authorization_code")
                    append("client_id", clientId)
                    append("client_secret", clientSecret)
                    append("code", authCode)
                    append("redirect_uri", redirectUri)
                })
            }
            val tokenResponse = response.body<AccessTokenResponse>()
            saveTokens(tokenResponse.accessToken, tokenResponse.refreshToken, tokenResponse.expiresIn)
            tokenResponse
        } catch (e: Exception) {
            clearTokens()
            null
        }
    }

    suspend fun refreshAccessToken(): AccessTokenResponse? {
        val currentRefreshToken = getRefreshToken()
        if (currentRefreshToken == null) {
            clearTokens()
            return null
        }

        return try {
            val response = unauthenticatedHttpClient.post(Url("https://shikimori.one/oauth/token")) {
                headers {
                    append(HttpHeaders.UserAgent, applicationName)
                }
                setBody(formData {
                    append("grant_type", "refresh_token")
                    append("client_id", clientId)
                    append("client_secret", clientSecret)
                    append("refresh_token", currentRefreshToken)
                })
            }
            val tokenResponse = response.body<AccessTokenResponse>()
            saveTokens(tokenResponse.accessToken, tokenResponse.refreshToken, tokenResponse.expiresIn)
            tokenResponse
        } catch (e: Exception) {
            clearTokens()
            null
        }
    }
}