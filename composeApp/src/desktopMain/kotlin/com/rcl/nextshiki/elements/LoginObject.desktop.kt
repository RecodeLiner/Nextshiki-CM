package com.rcl.nextshiki.elements

import Nextshiki.composeApp.BuildConfig.CLIENT_ID_DESK
import Nextshiki.composeApp.BuildConfig.DOMAIN
import Nextshiki.composeApp.BuildConfig.REDIRECT_URI_DESK
import Nextshiki.composeApp.BuildConfig.SCOPE_DESK
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rcl.nextshiki.*
import com.rcl.nextshiki.MR.strings.enter_code
import com.rcl.nextshiki.MR.strings.enter_ref_code
import com.rcl.nextshiki.MR.strings.login
import com.rcl.nextshiki.MR.strings.not_logged_in
import com.rcl.nextshiki.di.ktor.KtorModel
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.screens.profile.ProfileViewModel
import com.russhwolf.settings.set
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
internal actual fun LoginObject(vm: ProfileViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Center,
        horizontalAlignment = CenterHorizontally
    ) {
        var await by remember { mutableStateOf(false) }
        var code by remember { mutableStateOf("") }
        if (!await) {
            Text(
                text = getString(not_logged_in)
            )
            Button(
                onClick = {
                    val responseType = "code"
                    val url =
                        "$DOMAIN/oauth/authorize?client_id=$CLIENT_ID_DESK&redirect_uri=${
                            REDIRECT_URI_DESK.replace(
                                ":",
                                "%3A"
                            ).replace("/", "%2F")
                        }&response_type=${responseType}&scope=$SCOPE_DESK"
                    openUrl(url)
                    await = true
                },
                content = {
                    Text(
                        text = getString(login)
                    )
                }
            )
        } else {
            Text(
                text = getString(enter_ref_code)
            )
            OutlinedTextField(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                value = code,
                onValueChange = {
                    code = it
                }
            )
            Button(
                modifier = Modifier.padding(top = 10.dp),
                onClick = {
                    GlobalScope.launch {
                        val token = getToken(
                            isFirst = true,
                            code = code
                        )
                        if (token.error == null) {
                            settings["authCode"] = code
                            settings["refCode"] = token.refreshToken!!
                            KtorModel.token.value = token.accessToken!!
                            KtorModel.scope.value = token.scope!!

                            val obj = koin.get<KtorRepository>().getCurrentUser()

                            vm.getProfileObject(obj!!.id!!)

                            vm.isAuth.value=true
                        }
                    }
                },
                content = {
                    Text(
                        getString(enter_code)
                    )
                }
            )
        }
    }
}