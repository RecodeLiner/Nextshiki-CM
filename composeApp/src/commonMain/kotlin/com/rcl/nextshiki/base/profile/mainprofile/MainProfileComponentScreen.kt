package com.rcl.nextshiki.base.profile.mainprofile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.moko.MR.strings.copy_id
import com.rcl.moko.MR.strings.copy_link
import com.rcl.moko.MR.strings.logout
import com.rcl.moko.MR.strings.open_link_in_browser
import com.rcl.moko.MR.strings.settings
import com.rcl.nextshiki.base.profile.mainprofile.profile.AuthProfileObject
import com.rcl.nextshiki.base.profile.mainprofile.profile.ProfileObject
import dev.icerock.moko.resources.compose.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainProfileComponentScreen(component: MainProfileComponent) {
    val mainObject by component.mainAuthedObject.subscribeAsState()
    Scaffold(topBar = {
        CenterAlignedTopAppBar(windowInsets = WindowInsets(0), title = {
            mainObject.nickname?.let {
                Text(
                    text = it
                )
            }
        }, actions = {
            if (component.isAuth.value) {
                IconButton(onClick = { component.navigateToHistory() }) {
                    Icon(Icons.Default.History, "History navigate icon")
                }
            }
            Column {
                var expanded by remember { mutableStateOf(false) }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.Menu, "Open dropdownMenu")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    if (!mainObject.url.isNullOrEmpty()) {
                        val urlHandler = LocalUriHandler.current
                        DropdownMenuItem(onClick = { urlHandler.openUri(mainObject.url!!) }, text = {
                            Text(
                                stringResource(open_link_in_browser)
                            )
                        })
                        val copyManager = LocalClipboardManager.current
                        DropdownMenuItem(onClick = { copyManager.setText(AnnotatedString(text = mainObject.url!!)) },
                            text = {
                                Text(
                                    stringResource(copy_link)
                                )
                            })
                        mainObject.id?.let { id ->
                            DropdownMenuItem(onClick = { copyManager.setText(AnnotatedString(text = id.toString())) },
                                text = {
                                    Text(
                                        stringResource(copy_id)
                                    )
                                })
                        }
                    }
                    DropdownMenuItem(onClick = { component.navigateToSettings() }, text = {
                        Text(
                            stringResource(settings)
                        )
                    })
                    if (component.isAuth.value) {
                        DropdownMenuItem(onClick = { component.logout() }, text = {
                            Text(
                                stringResource(logout)
                            )
                        })
                    }
                }
            }
        })
    }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (component.isAuth.value) {
                if (mainObject.id != null) {
                    ProfileObject(mainObject, component::addToFriends, component::ignore)
                }
            } else {
                AuthProfileObject(component.ktorRepository, component::updateAuthState, component.settings)
            }
        }
    }
}