package com.rcl.nextshiki

import Nextshiki.composeApp.BuildConfig
import Nextshiki.composeApp.BuildConfig.CLIENT_ID
import Nextshiki.composeApp.BuildConfig.CLIENT_SECRET_DESK
import Nextshiki.composeApp.BuildConfig.REDIRECT_URI
import androidx.compose.runtime.Composable
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.currentuser.TokenModel
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.defaultImageResultMemoryCache
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.format
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okio.Path.Companion.toPath
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

internal actual fun openUrl(url: String?) {
    val nsUrl = url?.let { NSURL.URLWithString(it) } ?: return
    UIApplication.sharedApplication.openURL(nsUrl)
}

internal actual fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        components {
            setupDefaultComponents()
        }
        interceptor {
            defaultImageResultMemoryCache()
            memoryCacheConfig {
                maxSizeBytes(32 * 1024 * 1024)
            }
            diskCacheConfig {
                directory(getCacheDir().toPath().resolve("image_cache"))
                maxSizeBytes(512L * 1024 * 1024)
            }
        }
    }
}

private fun getCacheDir(): String {
    return NSSearchPathForDirectoriesInDomains(
        NSCachesDirectory,
        NSUserDomainMask,
        true,
    ).first() as String
}

@Composable
actual fun getString(id: StringResource, vararg args: List<Any>): String {
    return if (args.isEmpty()) {
        StringDesc.Resource(id).localized()
    } else {
        id.format(args).localized()
    }
}

internal actual suspend fun getToken(isFirst: Boolean, code: String): TokenModel {
    return koin.get<KtorRepository>().getToken(
        isFirst = isFirst,
        code = code,
        clientID = CLIENT_ID,
        clientSecret = CLIENT_SECRET_DESK,
        redirectUri = REDIRECT_URI
        )
}