package com.rcl.nextshiki.screens.search.searchelement

import Nextshiki.composeApp.BuildConfig
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rcl.nextshiki.MR
import com.rcl.nextshiki.elements.GetDropdownMenu
import com.rcl.nextshiki.elements.GetDropdownMenuItem
import com.rcl.nextshiki.getString
import com.rcl.nextshiki.openUrl

class SearchElementScreen(private val type: String, private val id: String) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        var expanded by remember { mutableStateOf(false) }
        val clipboardManager = LocalClipboardManager.current
        val navigator = LocalNavigator.currentOrThrow
        val vm = rememberScreenModel { SearchElementScreenModel(type, id) }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() },
                            content = {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    contentDescription = "Back from element"
                                )
                            }
                        )
                    },
                    title = {
                        Text(
                            text = vm.titleName.value
                        )
                    },
                    actions = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Search element menu icon"
                            )
                        }
                        GetDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false })
                        {
                            GetDropdownMenuItem(
                                text = { Text(text = getString(MR.strings.open_in_browser)) },
                                onClick = {
                                    expanded = false
                                    openUrl(BuildConfig.DOMAIN+vm.CurrObj.value!!.url!!)
                                }
                            )
                            GetDropdownMenuItem(
                                text = { Text(text = getString(MR.strings.copy_link)) },
                                onClick = {
                                    expanded = false
                                    clipboardManager.setText(AnnotatedString(BuildConfig.DOMAIN+vm.CurrObj.value!!.url!!))
                                }
                            )
                        }
                    }
                )
            }
        ) {

        }
    }
}