package com.rcl.nextshiki.elements

import Nextshiki.composeApp.BuildConfig.CLIENT_ID_DESK
import Nextshiki.composeApp.BuildConfig.CLIENT_SECRET_DESK
import Nextshiki.composeApp.BuildConfig.REDIRECT_URI_DESK
import com.rcl.nextshiki.di.ktor.KtorModuleObject
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.di.settings.ISettingsRepo
import com.rcl.nextshiki.elements.Platforms.Desktop
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp

internal actual fun currentPlatform(): Platforms {
    return Desktop
}

internal actual suspend fun updateToken(ktorRepository: KtorRepository, settings: ISettingsRepo) {
    val code = settings.getValue("refCode") ?: return

    val token = ktorRepository.getToken(
        code = code,
        clientSecret = CLIENT_SECRET_DESK,
        clientID = CLIENT_ID_DESK,
        redirectUri = REDIRECT_URI_DESK,
    )

    settings.addValue(key = "refCode", value = token.refreshToken.toString())
    if (token.accessToken != null) {
        KtorModuleObject.token.value = token.accessToken
    }
    if (token.scope != null) {
        KtorModuleObject.scope.value = token.scope
    }
}

actual fun getPlatformHttpClient(): HttpClientEngineFactory<HttpClientEngineConfig> {
    return OkHttp
}
