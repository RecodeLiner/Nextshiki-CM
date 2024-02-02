package com.rcl.nextshiki

import Nextshiki.composeApp.BuildConfig
import android.app.Application
import android.app.assist.AssistContent
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.retainedComponent
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.base.navEnabled
import com.rcl.nextshiki.di.Koin.getSafeKoin
import com.rcl.nextshiki.di.ktor.KtorModel
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.elements.currLink
import com.rcl.nextshiki.elements.getToken
import com.rcl.nextshiki.elements.settings
import com.russhwolf.settings.set
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.defaultImageResultMemoryCache
import com.seiko.imageloader.option.androidContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okio.Path.Companion.toOkioPath

class AndroidApp : Application() {
    companion object {
        lateinit var INSTANCE: AndroidApp
        lateinit var imageLoader: ImageLoader
        lateinit var clipboardManager: ClipboardManager
    }

    override fun onCreate() {
        super.onCreate()
        clipboardManager = getSystemService(this, ClipboardManager::class.java)!!
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
        if (currLink.value != null) {
            outContent.webUri = Uri.parse(currLink.value)
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
            App(root, seedColor = if (VERSION.SDK_INT > Build.VERSION_CODES.S) {
                dynamicDarkColorScheme(this).primary
            }
            else {
                Color.Blue
            }
            )
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onNewIntent(intent: Intent) {
        if (intent.data != null) {
            if (!intent.data.toString().startsWith("nextshiki:")) {
                getLink(intent.data.toString())
            } else {
                navEnabled.value = false
                GlobalScope.launch {
                    val code = intent.data.toString().split("code=")[1]
                    val token = getToken(isFirst = true, code = code)
                    if (token.error == null) {
                        settings["authCode"] = code
                        KtorModel.token.value = token.accessToken!!
                        KtorModel.scope.value = token.scope!!
                        settings["refCode"] = token.refreshToken!!

                        val obj = getSafeKoin().get<KtorRepository>().getCurrentUser()
                        settings["id"] = obj!!.id!!
                    }

                    navEnabled.value = true
                }
            }
        }
        super.onNewIntent(intent)
    }
}