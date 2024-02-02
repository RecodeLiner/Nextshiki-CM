package com.rcl.nextshiki.elements

import Nextshiki.composeApp.BuildConfig
import OperatingSystem
import OperatingSystem.*
import com.rcl.nextshiki.di.Koin
import com.rcl.nextshiki.di.Koin.getSafeKoin
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.currentuser.TokenModel
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.defaultImageResultMemoryCache
import okio.Path.Companion.toOkioPath
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.io.File
import java.net.URI

internal actual fun openUrl(url: String?) {
    val uri = url?.let { URI.create(it) } ?: return
    Desktop.getDesktop().browse(uri)
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
                directory(getCacheDir().toOkioPath().resolve("image_cache"))
                maxSizeBytes(512L * 1024 * 1024)
            }
        }
    }
}

internal actual fun copyToClipboard(text: String) {
    val selection = StringSelection(text)
    val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(selection, selection)
}

internal actual suspend fun getToken(
    isFirst: Boolean,
    code: String
): TokenModel {
    return getSafeKoin().get<KtorRepository>().getToken(
        isFirst = isFirst,
        code = code,
        clientID = BuildConfig.CLIENT_ID_DESK,
        clientSecret = BuildConfig.CLIENT_SECRET_DESK,
        redirectUri = BuildConfig.REDIRECT_URI_DESK.replace(
            ":",
            "%3A"
        ).replace("/", "%2F")
    )
}

private fun getCacheDir() = when (currentOperatingSystem) {
    Windows -> File(System.getenv("AppData"), "${BuildConfig.USER_AGENT}/cache")
    Linux -> File(System.getProperty("user.home"), ".cache/${BuildConfig.USER_AGENT}")
    MacOS -> File(System.getProperty("user.home"), "Library/Caches/${BuildConfig.USER_AGENT}")
    else -> throw IllegalStateException("Unsupported operating system")
}

private val currentOperatingSystem: OperatingSystem
    get() {
        val operSys = System.getProperty("os.name").lowercase()
        return if (operSys.contains("win")) {
            Windows
        } else if (operSys.contains("nix") || operSys.contains("nux") ||
            operSys.contains("aix")
        ) {
            Linux
        } else if (operSys.contains("mac")) {
            MacOS
        } else {
            Unknown
        }
    }