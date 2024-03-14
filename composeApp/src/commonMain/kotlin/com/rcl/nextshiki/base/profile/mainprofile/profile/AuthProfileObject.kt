package com.rcl.nextshiki.base.profile.mainprofile.profile

import Nextshiki.composeApp.BuildConfig.CLIENT_ID
import Nextshiki.composeApp.BuildConfig.CLIENT_ID_DESK
import Nextshiki.composeApp.BuildConfig.CLIENT_SECRET_DESK
import Nextshiki.composeApp.BuildConfig.DOMAIN
import Nextshiki.composeApp.BuildConfig.REDIRECT_URI
import Nextshiki.composeApp.BuildConfig.REDIRECT_URI_DESK
import Nextshiki.composeApp.BuildConfig.SCOPE
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.rcl.moko.MR.strings.login
import com.rcl.moko.MR.strings.not_logged_in
import com.rcl.nextshiki.di.ktor.KtorModel
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.elements.Platforms.Desktop
import com.rcl.nextshiki.elements.Platforms.Mobile
import com.rcl.nextshiki.elements.currentPlatform
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AuthProfileObject(ktorRepository: KtorRepository, updateState: (Boolean) -> Unit) {
    val platform = currentPlatform()
    when (platform) {
        Mobile -> {
            MobileAuth(updateState)
        }

        Desktop -> {
            DesktopAuth(ktorRepository, updateState)
        }
    }
}

@Composable
fun MobileAuth(updateState: (Boolean) -> Unit) {
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
        Column(modifier = Modifier.align(Center)) {
            Text(
                text = stringResource(not_logged_in)
            )
            Button(
                modifier = Modifier.padding(top = 10.dp),
                onClick = {
                    linkHandler.openUri(url)
                    updateState(true)
                }
            ) {
                Text(
                    text = stringResource(login)
                )
            }
        }
    }
}

@Composable
fun DesktopAuth(ktorRepository: KtorRepository, updateState: (Boolean) -> Unit) {
    val isError = mutableStateOf(false)
    val coroutine = rememberCoroutineScope()
    var enteredText by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Center)) {
            Text(
                text = stringResource(not_logged_in)
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
                    coroutine.launch {
                        val token = ktorRepository.getToken(
                            isFirst = true,
                            code = enteredText,
                            clientID = CLIENT_ID_DESK,
                            clientSecret = CLIENT_SECRET_DESK,
                            redirectUri = REDIRECT_URI_DESK
                        )
                        if (token.error == null) {
                            val settings = Settings()
                            settings["authCode"] = enteredText
                            settings["refCode"] = token.refreshToken!!
                            KtorModel.token.value = token.accessToken!!
                            KtorModel.scope.value = token.scope!!
                            updateState(true)
                        } else {
                            isError.value = true
                            delay(1500)
                            isError.value = false
                        }
                    }
                }
            ) {
                Text(
                    text = stringResource(login)
                )
            }
        }
    }
}