package com.rcl.nextshiki.data.api

import com.rcl.nextshiki.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom

class NSHttpClient(
    private val httpClient: HttpClient,
    private val tokenManager: TokenManager,
    private val baseUrl: String = BuildKonfig.domain?:"https://shikimori.one"
) : AuthorizedHttpClient {

    private suspend fun executeRequest(
        method: HttpMethod,
        path: String,
        block: HttpRequestBuilder.() -> Unit
    ): HttpResponse {
        val requestBuilder = HttpRequestBuilder()
        requestBuilder.method = method
        requestBuilder.header(HttpHeaders.UserAgent, BuildKonfig.appName)
        requestBuilder.url.takeFrom(URLBuilder(baseUrl).apply {
            appendPathSegments(path)
        })
        block(requestBuilder)

        if (tokenManager.isUserLoggedIn()) {
            var currentAccessToken = tokenManager.getAccessToken()

            if (currentAccessToken == null || !tokenManager.isAccessTokenCurrentlyValid()) {
                val refreshedTokens = tokenManager.refreshAccessToken()
                if (refreshedTokens != null) {
                    currentAccessToken = refreshedTokens.accessToken
                } else {
                    tokenManager.clearTokens()
                    throw ReauthenticationRequiredException("Failed to refresh token. Please re-authenticate.")
                }
            }

            requestBuilder.header(HttpHeaders.Authorization, "Bearer $currentAccessToken")
        }

        return httpClient.request(requestBuilder)
    }

    override suspend fun get(path: String, block: HttpRequestBuilder.() -> Unit): HttpResponse {
        return executeRequest(HttpMethod.Get, path, block)
    }

    override suspend fun post(path: String, block: HttpRequestBuilder.() -> Unit): HttpResponse {
        return executeRequest(HttpMethod.Post, path, block)
    }

    override suspend fun put(path: String, block: HttpRequestBuilder.() -> Unit): HttpResponse {
        return executeRequest(HttpMethod.Put, path, block)
    }

    override suspend fun delete(path: String, block: HttpRequestBuilder.() -> Unit): HttpResponse {
        return executeRequest(HttpMethod.Delete, path, block)
    }

    override suspend fun patch(path: String, block: HttpRequestBuilder.() -> Unit): HttpResponse {
        return executeRequest(HttpMethod.Patch, path, block)
    }

    override suspend fun head(path: String, block: HttpRequestBuilder.() -> Unit): HttpResponse {
        return executeRequest(HttpMethod.Head, path, block)
    }

    override suspend fun options(path: String, block: HttpRequestBuilder.() -> Unit): HttpResponse {
        return executeRequest(HttpMethod.Options, path, block)
    }
}
