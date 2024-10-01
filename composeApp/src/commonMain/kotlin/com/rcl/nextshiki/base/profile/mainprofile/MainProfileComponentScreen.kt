package com.rcl.nextshiki.base.profile.mainprofile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.mr.SharedRes.strings.copy_id
import com.rcl.mr.SharedRes.strings.copy_link
import com.rcl.mr.SharedRes.strings.logout
import com.rcl.mr.SharedRes.strings.open_link_in_browser
import com.rcl.mr.SharedRes.strings.settings
import com.rcl.nextshiki.base.profile.mainprofile.profile.AuthProfileObject
import com.rcl.nextshiki.base.profile.mainprofile.profile.ProfileObject
import com.rcl.nextshiki.locale.CustomLocale.getLocalizableString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainProfileComponentScreen(component: MainProfileComponent) {
    val isAuth by component.isAuth.subscribeAsState()
    val mainObject by component.mainAuthedObject.subscribeAsState()
    Scaffold(topBar = {
        CenterAlignedTopAppBar(windowInsets = WindowInsets(0), title = {
            mainObject.nickname?.let {
                Text(
                    text = it, maxLines = 2, overflow = TextOverflow.Ellipsis
                )
            }
        }, actions = {
            ActionBlock(
                isAuth = isAuth,
                navigateToHistory = component::navigateToHistory,
                url = mainObject.url,
                id = mainObject.id,
                navigateToSettings = component::navigateToSettings,
                logoutFun = component::logout
            )
        })
    }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (isAuth) {
                if (mainObject.id != null) {
                    ProfileObject(
                        data = mainObject,
                        friendFun = component::addToFriends,
                        ignoreFun = component::ignore,
                        navigateTo = component::navigateToContent
                    )
                }
            } else {
                AuthProfileObject(
                    ktorRepository = component.ktorRepository,
                    updateState = component::updateAuthState,
                    settings = component.settings
                )
            }
        }
    }
}

@Composable
private fun ActionBlock(
    isAuth: Boolean,
    navigateToHistory: () -> Unit,
    navigateToSettings: () -> Unit,
    logoutFun: () -> Unit,
    url: String?,
    id: Int?,
) {
    if (isAuth) {
        IconButton(onClick = { navigateToHistory() }) {
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
            if (!url.isNullOrEmpty()) {
                AuthedBlock(
                    expandedChange = {
                        expanded = it
                    },
                    url = url,
                    id = id
                )
            }
            DropdownMenuItem(onClick = {
                navigateToSettings()
                expanded = false
            }, text = {
                Text(
                    settings.getLocalizableString()
                )
            })
            if (isAuth) {
                DropdownMenuItem(onClick = {
                    logoutFun()
                    expanded = false
                }, text = {
                    Text(
                        logout.getLocalizableString()
                    )
                })
            }
        }
    }
}

@Composable
private fun AuthedBlock(
    url: String,
    expandedChange: (Boolean) -> Unit,
    id: Int?
) {
    val urlHandler = LocalUriHandler.current
    DropdownMenuItem(onClick = {
        urlHandler.openUri(url)
        expandedChange(false)
    }, text = {
        Text(
            open_link_in_browser.getLocalizableString()
        )
    })
    val copyManager = LocalClipboardManager.current
    DropdownMenuItem(onClick = {
        copyManager.setText(AnnotatedString(text = url))
        expandedChange(false)
    },
        text = {
            Text(
                copy_link.getLocalizableString()
            )
        })
    id?.let {
        DropdownMenuItem(onClick = {
            copyManager.setText(AnnotatedString(text = id.toString()))
            expandedChange(false)
        },
            text = {
                Text(
                    copy_id.getLocalizableString()
                )
            })
    }
}
