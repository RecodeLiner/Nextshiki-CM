package com.rcl.nextshiki.di.ktor

import Nextshiki.composeApp.BuildConfig
import androidx.compose.runtime.mutableStateOf
import com.rcl.nextshiki.elements.getPlatformHttpClient
import io.ktor.client.HttpClient
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

object KtorModuleObject {
    val networkModule = module {
        single {
            val userAgent = BuildConfig.USER_AGENT

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
                    if (token.value.isNotBlank()) {
                        header("Authorization", "Bearer ${token.value}")
                    }
                }
            }
        }

        single {
            KtorRepository(get())
        }
    }
    var token = mutableStateOf("")
    var scope = mutableStateOf("")
}
