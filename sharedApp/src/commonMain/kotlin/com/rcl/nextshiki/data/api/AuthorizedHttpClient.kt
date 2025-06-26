package com.rcl.nextshiki.data.api

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse

interface AuthorizedHttpClient  {
    suspend fun get(path: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse
    suspend fun post(path: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse
    suspend fun put(path: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse
    suspend fun delete(path: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse
    suspend fun patch(path: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse
    suspend fun head(path: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse
    suspend fun options(path: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse
}