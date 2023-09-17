package com.rcl.nextshiki.elements

import Nextshiki.composeApp.BuildConfig
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rcl.nextshiki.MR
import com.rcl.nextshiki.getString
import com.rcl.nextshiki.openUrl
import com.rcl.nextshiki.screens.profile.ProfileViewModel

@OptIn(InternalVoyagerApi::class)
@Composable
internal actual fun LoginObject(vm: ProfileViewModel) {
    val navigator = LocalNavigator.currentOrThrow
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = getString(MR.strings.not_logged_in)
        )
        Button(
            modifier = Modifier.padding(top = 10.dp),
            onClick = {
                val responseType = "code"
                val url =
                    "${BuildConfig.DOMAIN}/oauth/authorize?client_id=${BuildConfig.CLIENT_ID}&redirect_uri=${
                        BuildConfig.REDIRECT_URI.replace(
                            ":",
                            "%3A"
                        ).replace("/", "%2F")
                    }&response_type=${responseType}&scope=${BuildConfig.SCOPE}"
                openUrl(url)
                navigator.dispose(navigator.lastItem)
                navigator.popUntilRoot()
            },
            content = {
                Text(
                    text = getString(MR.strings.login)
                )
            }
        )
    }
}