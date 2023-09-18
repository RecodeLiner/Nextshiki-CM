package com.rcl.nextshiki

import Nextshiki.composeApp.BuildConfig
import android.app.Application
import android.app.assist.AssistContent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.rcl.nextshiki.AppActivity.Companion.context
import com.rcl.nextshiki.MatTheme.AppTheme
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
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
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

    companion object {
        lateinit var context: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        context = this.applicationContext
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AppTheme {
                setupKoin()
                setupUI()
            }
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
    AndroidApp.INSTANCE.startActivity(intent)
}

internal actual fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        options {
            androidContext(context)
        }
        components {
            setupDefaultComponents()
        }
        interceptor {
            defaultImageResultMemoryCache()
            memoryCacheConfig {
                maxSizePercent(context, 0.25)
            }
            diskCacheConfig {
                directory(context.cacheDir.resolve("image_cache").toOkioPath())
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}

@Composable
actual fun getString(id: StringResource, vararg args: List<Any>): String {
    return if (args.isEmpty()){
        StringDesc.Resource(id).toString(LocalContext.current)
    }
    else{
        id.format(args).toString(LocalContext.current)
    }
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