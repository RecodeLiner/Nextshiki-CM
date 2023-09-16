package com.rcl.nextshiki

import Nextshiki.composeApp.BuildConfig.CLIENT_ID_DESK
import Nextshiki.composeApp.BuildConfig.CLIENT_SECRET_DESK
import Nextshiki.composeApp.BuildConfig.REDIRECT_URI_DESK
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.currentuser.TokenModel

internal actual suspend fun getToken(isFirst: Boolean, code: String): TokenModel {
    return koin.get<KtorRepository>().getToken(
        isFirst = isFirst,
        code = code,
        clientID = CLIENT_ID_DESK,
        clientSecret = CLIENT_SECRET_DESK,
        redirectUri = REDIRECT_URI_DESK.replace(
            ":",
            "%3A"
        ).replace("/", "%2F")
    )
}