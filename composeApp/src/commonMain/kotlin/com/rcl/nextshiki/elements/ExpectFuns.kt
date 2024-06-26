package com.rcl.nextshiki.elements

import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.di.settings.SettingsRepo
import io.ktor.client.engine.*

internal expect fun copyToClipboard(text: String)
internal expect fun currentPlatform(): Platforms

enum class Platforms {
    Mobile,
    Desktop
}

expect fun getPlatformHttpClient(): HttpClientEngineFactory<HttpClientEngineConfig>

internal expect suspend fun updateToken(ktorRepository: KtorRepository, settings: SettingsRepo)