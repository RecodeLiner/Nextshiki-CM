package com.rcl.nextshiki.elements

import Nextshiki.composeApp.BuildConfig.CLIENT_ID_DESK
import Nextshiki.composeApp.BuildConfig.CLIENT_SECRET_DESK
import Nextshiki.composeApp.BuildConfig.REDIRECT_URI_DESK
import com.rcl.nextshiki.di.ktor.KtorModel
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.di.settings.SettingsRepo
import com.rcl.nextshiki.elements.Platforms.Desktop
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection


internal actual fun copyToClipboard(text: String) {
    val selection = StringSelection(text)
    val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(selection, selection)
}

internal actual fun currentPlatform(): Platforms {
    return Desktop
}

internal actual suspend fun updateToken(ktorRepository: KtorRepository, settings: SettingsRepo) {
    val code = settings.getValue("refCode") ?: return

    val token = ktorRepository.getToken(
        code = code,
        clientSecret = CLIENT_SECRET_DESK,
        clientID = CLIENT_ID_DESK,
        redirectUri = REDIRECT_URI_DESK,
    )

    settings.addValue(key = "refCode", value = token.refreshToken.toString())
    if (token.accessToken != null){
        KtorModel.token.value = token.accessToken
    }
    if (token.scope != null) {
        KtorModel.scope.value = token.scope
    }
}

actual fun getPlatformHttpClient(): HttpClientEngineFactory<HttpClientEngineConfig> {
    return OkHttp
}