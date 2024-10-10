package com.rcl.nextshiki.compose.screens

import Nextshiki.resources.BuildConfig.CLIENT_ID_DESK
import Nextshiki.resources.BuildConfig.CLIENT_SECRET_DESK
import Nextshiki.resources.BuildConfig.DOMAIN
import Nextshiki.resources.BuildConfig.REDIRECT_URI_DESK
import Nextshiki.resources.BuildConfig.SCOPE_DESK
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
import com.rcl.nextshiki.SharedRes.strings.login
import com.rcl.nextshiki.SharedRes.strings.login_in_browser
import com.rcl.nextshiki.SharedRes.strings.not_logged_in
import com.rcl.nextshiki.components.profilecomponent.mainprofile.MainProfileComponent
import com.rcl.nextshiki.di.KtorRepository
import com.rcl.nextshiki.di.RepositoryModule
import com.rcl.nextshiki.di.settings.ISettingsRepo
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
actual fun AuthBlock(viewModel: MainProfileComponent.MainProfileViewModel) {
    val ktorRepository = viewModel.ktorRepository
    val linkHandler = LocalUriHandler.current
    var isError by mutableStateOf(false)
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
                text = stringResource(not_logged_in)
            )
            OutlinedTextField(
                modifier = Modifier.padding(top = 10.dp),
                isError = isError,
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
                    text = stringResource(login_in_browser)
                )
            }
            DesktopLoginButton(
                updateState = viewModel::updateAuthState,
                code = enteredText,
                errorUpdate = { isError = it },
                repo = ktorRepository,
                settings = viewModel.settings,
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

                    RepositoryModule.token = token.accessToken!!
                    RepositoryModule.scope = token.scope!!
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
            text = stringResource(login)
        )
    }
}