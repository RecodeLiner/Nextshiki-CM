package com.rcl.nextshiki.elements

import Nextshiki.composeApp.BuildConfig
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.currentuser.TokenModel
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.defaultImageResultMemoryCache
import okio.Path.Companion.toPath

internal actual fun openUrl(url: String?) {
    val nsUrl = url?.let { NSURL.URLWithString(it) } ?: return
    UIApplication.sharedApplication.openURL(nsUrl)
}

private fun getCacheDir(): String {
    return NSSearchPathForDirectoriesInDomains(
        NSCachesDirectory,
        NSUserDomainMask,
        true,
    ).first() as String
}

internal actual fun copyToClipboard(text: String) {

}