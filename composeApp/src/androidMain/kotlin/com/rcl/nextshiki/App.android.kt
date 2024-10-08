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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.retainedComponent
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.di.ktor.KtorModuleObject
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.di.settings.ISettingsRepo
import com.rcl.nextshiki.elements.ClipboardImpl
import com.rcl.nextshiki.elements.setupKoin
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()

        setupNapier()
        val clipboardService = getSystemService(this, ClipboardManager::class.java)
        startKoin {
            androidLogger()
            androidContext(this@AndroidApp)
            modules(
                *setupKoin(clipboard = ClipboardImpl(clipboardService))
            )
        }
    }
}

class AppActivity : ComponentActivity(), KoinComponent {
    private lateinit var component: RootComponent
    private val ktorRepository: KtorRepository by inject()
    private val settings: ISettingsRepo by inject()

    private val currentLink = mutableStateOf<String?>(null)

    override fun onProvideAssistContent(outContent: AssistContent?) {
        super.onProvideAssistContent(outContent)
        outContent?.webUri = if (currentLink.value == null) {
            null
        } else {
            Uri.parse(currentLink.value)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        component = retainedComponent {
            RootComponent(it)
        }
        setContent {
            App(
                component, seedColor = if (VERSION.SDK_INT > Build.VERSION_CODES.S) {
                    dynamicDarkColorScheme(this).primary
                } else {
                    Color.Blue
                }
            )
            val stateFlow by component.currentLinkFlow.collectAsState()
            LaunchedEffect(stateFlow) {
                currentLink.value = stateFlow
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        intent.data?.let { data ->
            when {
                data.toString().startsWith("nextshiki:") -> {
                    runBlocking {
                        handleAuthDeepLink(data.toString())
                    }
                }

                else -> {
                    val link = getLink(data.toString())
                    component.deepLinkHandler(link)
                }
            }
        }
        super.onNewIntent(intent)
    }

    private fun getLink(rawLink: String): String {
        if (rawLink.length > BuildConfig.DOMAIN.length) {
            val splittedLink = rawLink.split("/")
            val stringBuilder = StringBuilder(splittedLink[3])
            for (i in 4 until splittedLink.size) {
                stringBuilder.append("/").append(splittedLink[i])
            }
            return stringBuilder.toString()
        }
        return ""
    }

    private suspend fun handleAuthDeepLink(deepLink: String) {
        val code = deepLink.split("code=")[1]
        val token = ktorRepository.getToken(
            isFirst = true,
            code = code,
            clientID = CLIENT_ID,
            clientSecret = CLIENT_SECRET,
            redirectUri = REDIRECT_URI
        )
        if (token.error == null) {
            settings.addValue(key = "authCode", value = code)
            KtorModuleObject.token.value = token.accessToken.toString()
            KtorModuleObject.scope.value = token.scope.toString()
            token.refreshToken?.let { settings.addValue(key = "refCode", value = it) }

            val obj = ktorRepository.getCurrentUser()
            obj?.id?.let { settings.addValue(key = "id", value = it.toString()) }
        }
    }
}
