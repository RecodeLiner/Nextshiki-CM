package com.rcl.nextshiki

import Nextshiki.composeApp.BuildConfig
import android.app.Application
import android.app.assist.AssistContent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.retainedComponent
import com.rcl.nextshiki.AndroidApp.Companion.INSTANCE
import com.rcl.nextshiki.AndroidApp.Companion.imageLoader
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.di.ktor.KtorModel
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.currentuser.TokenModel
import com.russhwolf.settings.set
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.defaultImageResultMemoryCache
import com.seiko.imageloader.option.androidContext
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.format
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okio.Path.Companion.toOkioPath

class AndroidApp : Application() {
    companion object {
        lateinit var INSTANCE: AndroidApp
        lateinit var imageLoader: ImageLoader
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        imageLoader = ImageLoader {
            options {
                androidContext(applicationContext)
            }
            components {
                setupDefaultComponents()
            }
            interceptor {
                defaultImageResultMemoryCache()
                memoryCacheConfig {
                    maxSizePercent(applicationContext, 0.25)
                }
                diskCacheConfig {
                    directory(applicationContext.cacheDir.resolve("image_cache").toOkioPath())
                    maxSizeBytes(512L * 1024 * 1024) // 512MB
                }
            }
        }
    }
}

class AppActivity : ComponentActivity() {
    override fun onProvideAssistContent(outContent: AssistContent) {
        super.onProvideAssistContent(outContent)
        if (link.value != null) {
            outContent.webUri = Uri.parse(link.value)
        } else {
            outContent.webUri = null
        }
    }

    private var tempLink = ""

    private fun getLink(rawLink: String) {
        if (rawLink.length > BuildConfig.DOMAIN.length) {
            val splittedLink = rawLink.split("/")
            tempLink = splittedLink[3]
            for (i in 4 until splittedLink.size) {
                tempLink += "/" + splittedLink[i]
            }
        }
    }

    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val root = retainedComponent {
            RootComponent(it)
        }
        setContent {
            App(root)
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    override fun onNewIntent(intent: Intent) {
        if (intent.data != null) {
            if (!intent.data.toString().startsWith("nextshiki:")) {
                getLink(intent.data.toString())
            } else {
                navEnabled.value = false
                GlobalScope.launch{
                    val code = intent.data.toString().split("code=")[1]
                    val token = getToken(isFirst = true, code = code)
                    if (token.error==null){
                        settings["authCode"] = code
                        KtorModel.token.value = token.accessToken!!
                        KtorModel.scope.value = token.scope!!
                        settings["refCode"] = token.refreshToken!!

                        val obj = koin.get<KtorRepository>().getCurrentUser()
                        settings["id"] = obj!!.id!!
                    }

                    navEnabled.value = true
                }
            }
        }
        super.onNewIntent(intent)
    }
}

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
    return koin.get<KtorRepository>().getToken(
        isFirst = isFirst,
        code = code,
        clientID = BuildConfig.CLIENT_ID,
        clientSecret = BuildConfig.CLIENT_SECRET,
        redirectUri = BuildConfig.REDIRECT_URI
    )
}