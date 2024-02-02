package com.rcl.nextshiki.elements

import Nextshiki.composeApp.BuildConfig.CLIENT_ID
import Nextshiki.composeApp.BuildConfig.CLIENT_SECRET
import Nextshiki.composeApp.BuildConfig.REDIRECT_URI
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import com.rcl.nextshiki.AndroidApp.Companion.INSTANCE
import com.rcl.nextshiki.AndroidApp.Companion.clipboardManager
import com.rcl.nextshiki.AndroidApp.Companion.imageLoader
import com.rcl.nextshiki.di.Koin
import com.rcl.nextshiki.di.Koin.getSafeKoin
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.currentuser.TokenModel
import com.seiko.imageloader.ImageLoader

internal actual fun openUrl(url: String?) {
    val uri = url?.let { Uri.parse(it) } ?: return
    val intent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = uri
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    INSTANCE.startActivity(intent)
}

internal actual fun generateImageLoader(): ImageLoader {
    return imageLoader
}

internal actual suspend fun getToken(isFirst: Boolean, code: String): TokenModel {
    return getSafeKoin().get<KtorRepository>().getToken(
        isFirst = isFirst,
        code = code,
        clientID = CLIENT_ID,
        clientSecret = CLIENT_SECRET,
        redirectUri = REDIRECT_URI
    )
}

internal actual fun copyToClipboard(text: String) {
    val clip = ClipData.newPlainText("label", text)
    clipboardManager.setPrimaryClip(clip)
}