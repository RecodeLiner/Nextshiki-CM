package com.rcl.nextshiki

import Nextshiki.composeApp.BuildConfig
import Nextshiki.composeApp.BuildConfig.CLIENT_ID
import Nextshiki.composeApp.BuildConfig.CLIENT_SECRET
import Nextshiki.composeApp.BuildConfig.REDIRECT_URI
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
import com.rcl.nextshiki.di.ktor.KtorModel
import com.rcl.nextshiki.di.ktor.KtorModel.networkModule
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.elements.currLink
import com.rcl.nextshiki.elements.settings
import com.russhwolf.settings.set
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class AndroidApp : Application() {
    companion object {
        lateinit var INSTANCE: AndroidApp
        lateinit var clipboardManager: ClipboardManager
    }

    override fun onCreate() {
        super.onCreate()
        clipboardManager = getSystemService(this, ClipboardManager::class.java)!!
        INSTANCE = this
        startKoin {
            modules(networkModule)
        }
    }
}

class AppActivity : ComponentActivity(), KoinComponent {
    private val ktorRepository: KtorRepository by inject()
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
            App(
                root, seedColor = if (VERSION.SDK_INT > Build.VERSION_CODES.S) {
                    dynamicDarkColorScheme(this).primary
                } else {
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
                    val token = ktorRepository.getToken(
                        isFirst = true,
                        code = code,
                        clientID = CLIENT_ID,
                        clientSecret = CLIENT_SECRET,
                        redirectUri = REDIRECT_URI
                    )
                    if (token.error == null) {
                        settings["authCode"] = code
                        KtorModel.token.value = token.accessToken!!
                        KtorModel.scope.value = token.scope!!
                        settings["refCode"] = token.refreshToken!!

                        val obj = ktorRepository.getCurrentUser()
                        settings["id"] = obj!!.id!!
                    }

                    navEnabled.value = true
                }
            }
        }
        super.onNewIntent(intent)
    }
}