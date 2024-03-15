package com.rcl.nextshiki.elements

import com.rcl.nextshiki.di.ktor.KtorRepository
import com.russhwolf.settings.Settings

internal expect fun copyToClipboard(text: String)
internal expect fun currentPlatform() : Platforms

enum class Platforms {
    Mobile,
    Desktop
}

internal expect suspend fun updateToken(ktorRepository: KtorRepository, settings: Settings)