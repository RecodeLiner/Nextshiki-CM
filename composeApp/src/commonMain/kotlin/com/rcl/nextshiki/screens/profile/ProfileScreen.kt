package com.rcl.nextshiki.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rcl.nextshiki.MR.strings.copy_link
import com.rcl.nextshiki.MR.strings.logout
import com.rcl.nextshiki.MR.strings.settings
import com.rcl.nextshiki.elements.GetDropdownMenu
import com.rcl.nextshiki.elements.GetDropdownMenuItem
import com.rcl.nextshiki.elements.LoginObject
import com.rcl.nextshiki.elements.ProfileObject
import com.rcl.nextshiki.getString
import com.rcl.nextshiki.screens.profile.settings.SettingsScreen
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class ProfileScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class, InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val vm = rememberScreenModel { ProfileViewModel() }
        val navigator = LocalNavigator.currentOrThrow
        var expanded by remember { mutableStateOf(false) }
        val clipboard = LocalClipboardManager.current

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(
                            content = {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = "Back from profile"
                                )
                            },
                            onClick = {
                                navigator.pop()
                            }
                        )
                    },
                    title = {
                        if (vm.isAuth.value && vm.profileObject.value != null) {
                            Text(
                                text = vm.profileObject.value!!.nickname!!
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            content = {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Open profile menu"
                                )
                            },
                            onClick = {
                                expanded = true
                            }
                        )
                        GetDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false })
                        {
                            GetDropdownMenuItem(
                                text = { Text(text = getString(settings)) },
                                onClick = {
                                    expanded = false
                                    navigator.push(SettingsScreen())
                                }
                            )
                            if (vm.isAuth.value) {
                                GetDropdownMenuItem(
                                    text = { Text(text = getString(copy_link)) },
                                    onClick = {
                                        expanded = false
                                        clipboard.setText(AnnotatedString(vm.profileObject.value!!.url!!))
                                    }
                                )
                                GetDropdownMenuItem(
                                    text = { Text(text = getString(logout)) },
                                    onClick = {
                                        GlobalScope.launch {
                                            expanded = false
                                            vm.logout()
                                            navigator.popUntilRoot()
                                        }
                                    }
                                )
                            }
                        }
                    }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                if (vm.isAuth.value) {
                    if (vm.profileObject.value != null) {
                        ProfileObject(
                            isCurrentUser = true,
                            value = vm.profileObject.value!!,
                            padding = PaddingValues(top = 36.dp),
                            addToFriend = {
                                GlobalScope.launch {
                                    vm.addToFriend()
                                }
                            }
                        )
                    }
                } else {
                    LoginObject(vm)
                }
            }
        }
    }
}