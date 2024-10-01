package com.rcl.nextshiki.base.profile.mainprofile.profile

import Nextshiki.composeApp.BuildConfig.CLIENT_ID
import Nextshiki.composeApp.BuildConfig.CLIENT_ID_DESK
import Nextshiki.composeApp.BuildConfig.CLIENT_SECRET_DESK
import Nextshiki.composeApp.BuildConfig.DOMAIN
import Nextshiki.composeApp.BuildConfig.REDIRECT_URI
import Nextshiki.composeApp.BuildConfig.REDIRECT_URI_DESK
import Nextshiki.composeApp.BuildConfig.SCOPE
import Nextshiki.composeApp.BuildConfig.SCOPE_DESK
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.rcl.mr.SharedRes.strings.login
import com.rcl.mr.SharedRes.strings.login_in_browser
import com.rcl.mr.SharedRes.strings.not_logged_in
import com.rcl.nextshiki.di.ktor.KtorModuleObject
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.di.settings.ISettingsRepo
import com.rcl.nextshiki.elements.Platforms.Desktop
import com.rcl.nextshiki.elements.Platforms.Mobile
import com.rcl.nextshiki.elements.currentPlatform
import com.rcl.nextshiki.locale.CustomLocale.getLocalizableString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AuthProfileObject(
    ktorRepository: KtorRepository,
    updateState: (Boolean) -> Unit,
    settings: ISettingsRepo
) {
    val platform = currentPlatform()
    when (platform) {
        Mobile -> {
            MobileAuth()
        }

        Desktop -> {
            DesktopAuth(ktorRepository, updateState, settings)
        }
    }
}

@Composable
fun MobileAuth() {
    val linkHandler = LocalUriHandler.current
    val responseType = "code"
    val url =
        "$DOMAIN/oauth/authorize?client_id=$CLIENT_ID&redirect_uri=${
            REDIRECT_URI.replace(
                ":",
                "%3A"
            ).replace("/", "%2F")
        }&response_type=${responseType}&scope=$SCOPE"
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Center), verticalArrangement = Arrangement.Center) {
            Text(
                text = not_logged_in.getLocalizableString()
            )
            Button(
                modifier = Modifier.padding(top = 10.dp),
                onClick = {
                    linkHandler.openUri(url)
                }
            ) {
                Text(
                    text = login.getLocalizableString()
                )
            }
        }
    }
}

@Composable
fun DesktopAuth(
    ktorRepository: KtorRepository,
    updateState: (Boolean) -> Unit,
    settings: ISettingsRepo
) {
    val linkHandler = LocalUriHandler.current
    val isError = mutableStateOf(false)
    var enteredText by remember { mutableStateOf("") }
    val responseType = "code"
    val url =
        "$DOMAIN/oauth/authorize?client_id=$CLIENT_ID_DESK&redirect_uri=${
            REDIRECT_URI_DESK.replace(
                ":",
                "%3A"
            ).replace("/", "%2F")
        }&response_type=${responseType}&scope=$SCOPE_DESK"
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Center)) {
            Text(
                text = not_logged_in.getLocalizableString()
            )
            OutlinedTextField(
                modifier = Modifier.padding(top = 10.dp),
                isError = isError.value,
                value = enteredText,
                onValueChange = {
                    enteredText = if (it.endsWith("\n")) {
                        it.dropLast(1)
                    } else {
                        it
                    }
                }
            )
            Button(
                modifier = Modifier.padding(top = 10.dp),
                onClick = {
                    linkHandler.openUri(url)
                }
            ) {
                Text(
                    text = login_in_browser.getLocalizableString()
                )
            }
            DesktopLoginButton(
                updateState = updateState,
                code = enteredText,
                errorUpdate = { isError.value = it },
                repo = ktorRepository,
                settings = settings,
            )
        }
    }
}

@Composable
private fun DesktopLoginButton(
    updateState: (Boolean) -> Unit,
    errorUpdate: (Boolean) -> Unit,
    repo: KtorRepository,
    settings: ISettingsRepo,
    code: String
) {
    val coroutine = rememberCoroutineScope()
    Button(
        modifier = Modifier.padding(top = 10.dp),
        onClick = {
            coroutine.launch {
                val token = repo.getToken(
                    isFirst = true,
                    code = code,
                    clientID = CLIENT_ID_DESK,
                    clientSecret = CLIENT_SECRET_DESK,
                    redirectUri = REDIRECT_URI_DESK
                )
                if (token.error == null) {
                    settings.addValue(key = "authCode", value = code)
                    settings.addValue(key = "refCode", value = token.refreshToken!!)
                    KtorModuleObject.token.value = token.accessToken!!
                    KtorModuleObject.scope.value = token.scope!!
                    updateState(true)
                } else {
                    errorUpdate(true)
                    delay(1500)
                    errorUpdate(false)
                }
            }
        }
    ) {
        Text(
            text = login.getLocalizableString()
        )
    }
}
