package com.rcl.nextshiki.compose.screens

import Nextshiki.resources.BuildConfig.CLIENT_ID
import Nextshiki.resources.BuildConfig.DOMAIN
import Nextshiki.resources.BuildConfig.REDIRECT_URI
import Nextshiki.resources.BuildConfig.SCOPE
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.rcl.nextshiki.SharedRes.strings.login
import com.rcl.nextshiki.SharedRes.strings.not_logged_in
import com.rcl.nextshiki.components.profilecomponent.mainprofile.MainProfileComponent
import dev.icerock.moko.resources.compose.stringResource

@Composable
actual fun AuthBlock(viewModel: MainProfileComponent.MainProfileViewModel) {
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
                text = stringResource(not_logged_in)
            )
            Button(
                modifier = Modifier.padding(top = 10.dp),
                onClick = {
                    linkHandler.openUri(url)
                }
            ) {
                Text(
                    text = stringResource(login)
                )
            }

        }
    }
}
