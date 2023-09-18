package com.rcl.nextshiki.screens.profile.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rcl.nextshiki.MR
import com.rcl.nextshiki.getString

class SettingsScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val colorScheme = MaterialTheme.colorScheme
        val vm = rememberScreenModel { SettingsViewModel() }
        val navigator = LocalNavigator.currentOrThrow
        val clipboard = LocalClipboardManager.current

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navigator.pop()
                            },
                            content = {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    "Back from settings"
                                )
                            }
                        )
                    },
                    title = {
                        Text(
                            text = getString(MR.strings.settings)
                        )
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                    Button(onClick = {
                        vm.copyColorScheme(colorScheme, clipboard)
                    },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        content = {
                            Text("Get current color Scheme")
                        }
                    )
                    Button(onClick = {
                        vm.copyToken(clipboard)
                    },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        content = {
                            Text(
                                text = "Get current token"
                            )
                        }
                    )
                }
            }
        }
    }
}