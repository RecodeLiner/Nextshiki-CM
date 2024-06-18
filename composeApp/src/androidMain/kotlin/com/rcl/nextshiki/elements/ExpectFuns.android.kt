package com.rcl.nextshiki.elements

import Nextshiki.composeApp.BuildConfig.CLIENT_ID
import Nextshiki.composeApp.BuildConfig.CLIENT_SECRET
import Nextshiki.composeApp.BuildConfig.REDIRECT_URI
import android.content.ClipData
import com.rcl.nextshiki.AndroidApp.Companion.clipboardManager
import com.rcl.nextshiki.di.ktor.KtorModel
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.di.settings.SettingsRepo
import com.rcl.nextshiki.elements.Platforms.Mobile
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*


internal actual fun copyToClipboard(text: String) {
    val clip = ClipData.newPlainText("label", text)
    clipboardManager.setPrimaryClip(clip)
}

internal actual fun currentPlatform(): Platforms {
    return Mobile
}

internal actual suspend fun updateToken(ktorRepository: KtorRepository, settings: SettingsRepo) {
    val code = settings.getValue("refCode") ?: return

    val token = ktorRepository.getToken(
        code = code,
        clientSecret = CLIENT_SECRET,
        clientID = CLIENT_ID,
        redirectUri = REDIRECT_URI,
    )

    settings.addValue(key = "refCode", value = token.refreshToken.toString())
    if (token.accessToken != null) {
        KtorModel.token.value = token.accessToken
    }
    if (token.scope != null) {
        KtorModel.scope.value = token.scope
    }
}

actual fun getPlatformHttpClient(): HttpClientEngineFactory<HttpClientEngineConfig> {
    return OkHttp
}