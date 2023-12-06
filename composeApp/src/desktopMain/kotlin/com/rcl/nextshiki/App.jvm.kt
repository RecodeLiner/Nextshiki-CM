package com.rcl.nextshiki

import Nextshiki.composeApp.BuildConfig
import Nextshiki.composeApp.BuildConfig.CLIENT_ID_DESK
import Nextshiki.composeApp.BuildConfig.CLIENT_SECRET_DESK
import Nextshiki.composeApp.BuildConfig.REDIRECT_URI_DESK
import androidx.compose.runtime.Composable
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.currentuser.TokenModel
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.defaultImageResultMemoryCache
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import okio.Path.Companion.toOkioPath
import java.awt.Desktop
import java.io.File
import java.net.URI

internal actual fun openUrl(url: String?) {
    val uri = url?.let { URI.create(it) } ?: return
    Desktop.getDesktop().browse(uri)
}

internal actual fun generateImageLoader(): ImageLoader{
    return ImageLoader {
        components {
            setupDefaultComponents()
        }
        interceptor {
            defaultImageResultMemoryCache()
            memoryCacheConfig {
                maxSizeBytes(32 * 1024 * 1024) // 32MB
            }
            diskCacheConfig {
                directory(getCacheDir().toOkioPath().resolve("image_cache"))
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}

enum class OperatingSystem {
    Windows, Linux, MacOS, Unknown
}

private val currentOperatingSystem: OperatingSystem
    get() {
        val operSys = System.getProperty("os.name").lowercase()
        return if (operSys.contains("win")) {
            OperatingSystem.Windows
        } else if (operSys.contains("nix") || operSys.contains("nux") ||
            operSys.contains("aix")
        ) {
            OperatingSystem.Linux
        } else if (operSys.contains("mac")) {
            OperatingSystem.MacOS
        } else {
            OperatingSystem.Unknown
        }
    }

private fun getCacheDir() = when (currentOperatingSystem) {
    OperatingSystem.Windows -> File(System.getenv("AppData"), "${BuildConfig.USER_AGENT}/cache")
    OperatingSystem.Linux -> File(System.getProperty("user.home"), ".cache/${BuildConfig.USER_AGENT}")
    OperatingSystem.MacOS -> File(System.getProperty("user.home"), "Library/Caches/${BuildConfig.USER_AGENT}")
    else -> throw IllegalStateException("Unsupported operating system")
}
@Suppress("UNREACHABLE_CODE")
@Composable
actual fun getString(id: StringResource, vararg args: List<Any>): String {
    return if (args.isEmpty()){
        return stringResource(id)
    }
    else{
        return stringResource(id, args)
    }
}
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