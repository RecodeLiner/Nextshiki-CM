package com.rcl.nextshiki.di

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

actual fun getPlatformHttpClient() : HttpClientEngineFactory<HttpClientEngineConfig> = Darwin