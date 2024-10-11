package com.rcl.nextshiki.di

import Nextshiki.resources.BuildConfig.USER_AGENT
import Nextshiki.resources.BuildConfig.USER_AGENT_DESK
import com.rcl.nextshiki.utils.Platform
import com.rcl.nextshiki.utils.getCurrentPlatform
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

object RepositoryModule {
    val networkModule = module {
        single {
            val userAgent = when(getCurrentPlatform()) {
                Platform.Mobile -> USER_AGENT
                Platform.Desktop -> USER_AGENT_DESK
            }

            HttpClient(getPlatformHttpClient()) {
                expectSuccess = true
                install(ContentNegotiation) {
                    json(Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        prettyPrint = true
                    })
                }
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL
                }
                defaultRequest {
                    header("User-Agent", userAgent)
                    if (token != null) {
                        header("Authorization", "Bearer $token")
                    }
                }
            }
        }

        single {
            KtorRepository(get())
        }
    }
    var token: String? = null
    var scope: String? = null
}

expect fun getPlatformHttpClient(): HttpClientEngineFactory<HttpClientEngineConfig>