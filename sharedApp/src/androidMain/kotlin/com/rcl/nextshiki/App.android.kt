package com.rcl.nextshiki

import Nextshiki.resources.BuildConfig.CLIENT_ID
import Nextshiki.resources.BuildConfig.CLIENT_SECRET
import Nextshiki.resources.BuildConfig.DOMAIN
import Nextshiki.resources.BuildConfig.REDIRECT_URI
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
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.retainedComponent
import com.rcl.nextshiki.components.RootComponent
import com.rcl.nextshiki.components.setupNapier
import com.rcl.nextshiki.compose.App
import com.rcl.nextshiki.di.KtorRepository
import com.rcl.nextshiki.di.RepositoryModule
import com.rcl.nextshiki.di.clipboard.ClipboardImpl
import com.rcl.nextshiki.di.settings.ISettingsRepo
import com.rcl.nextshiki.di.settings.SettingsModuleObject
import kotlinx.coroutines.runBlocking

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupNapier()
        ClipboardImpl.clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    }
}

class AppActivity : ComponentActivity() {
    private lateinit var component: RootComponent
    private val ktorRepository: KtorRepository = RepositoryModule.getKtorRepository()
    private val settings: ISettingsRepo = SettingsModuleObject.settingsImpl

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
            val stateFlow by component.currentLink.collectAsState()
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
                    component.vm.deepLinkHandler(link)
                }
            }
        }
        super.onNewIntent(intent)
    }

    private fun getLink(rawLink: String): String {
        if (rawLink.length > DOMAIN.length) {
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
            RepositoryModule.token = token.accessToken.toString()
            RepositoryModule.scope = token.scope.toString()
            token.refreshToken?.let { settings.addValue(key = "refCode", value = it) }

            val obj = ktorRepository.getCurrentUser()
            obj?.id?.let { settings.addValue(key = "id", value = it.toString()) }
        }
    }
}
