package com.rcl.nextshiki.elements

import Nextshiki.composeApp.BuildConfig.CLIENT_ID_DESK
import Nextshiki.composeApp.BuildConfig.CLIENT_SECRET_DESK
import Nextshiki.composeApp.BuildConfig.REDIRECT_URI_DESK
import com.rcl.nextshiki.di.ktor.KtorModel
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.elements.Platforms.Desktop
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
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

internal actual suspend fun updateToken(ktorRepository: KtorRepository, settings: Settings) {
    val code = settings.getStringOrNull("refCode") ?: return

    val token = ktorRepository.getToken(
        code = code,
        clientSecret = CLIENT_SECRET_DESK,
        clientID = CLIENT_ID_DESK,
        redirectUri = REDIRECT_URI_DESK,
    )

    settings["refCode"] = token.refreshToken
    if (token.accessToken != null){
        KtorModel.token.value = token.accessToken
    }
    if (token.scope != null) {
        KtorModel.scope.value = token.scope
    }
}