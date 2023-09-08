package com.rcl.nextshiki

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rcl.nextshiki.di.ktor.KtorModel
import com.rcl.nextshiki.screens.main.MainScreen
import com.rcl.nextshiki.screens.ScreenList
import com.rcl.nextshiki.theme.Theme.AppTheme
import com.seiko.imageloader.ImageLoader
import org.koin.core.Koin
import org.koin.core.context.startKoin

lateinit var navigatorLow: Navigator
lateinit var koin: Koin

@Composable
internal fun App() = AppTheme {
    SetupKoin()
}

@Composable
internal fun SetupKoin() {
    koin = startKoin { modules(KtorModel.networkModule) }.koin
    setupUI()
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun setupUI() {
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass
    Navigator(
        screen = MainScreen
    ) {
        when (widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                NavBar()
            }

            WindowWidthSizeClass.Medium -> {
                MediumScreen()
            }

            WindowWidthSizeClass.Expanded -> {
                ExpandedScreen()
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun NavBar() {
    val navigator = LocalNavigator.currentOrThrow
    Scaffold(
        content = {
            Box(modifier = Modifier.padding(it)) {
                navigator.lastItem.Content()
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background
            ) {
                ScreenList.screens.forEach { item ->
                    val selected = item.screen == navigator.lastItem
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navigator.push(item.screen)
                        },
                        label = {
                            Text(
                                text = item.name,
                                fontWeight = FontWeight.SemiBold,
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = if (selected) {
                                    item.filledIcon
                                } else {
                                    item.outlinedIcon
                                },
                                contentDescription = "Icon",
                            )
                        },
                        alwaysShowLabel = false
                    )
                }
            }
        }
    )
}

@ExperimentalMaterial3Api
@Composable
fun MediumScreen() {
    val navigator = LocalNavigator.currentOrThrow
    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail {
            ScreenList.screens.forEach { item ->
                val selected = item.screen == navigator.lastItem
                NavigationRailItem(
                    selected = selected,
                    onClick = {
                        navigator.push(item.screen)
                    },
                    label = {
                        Text(
                            text = item.name,
                            fontWeight = FontWeight.SemiBold,
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = if (selected) {
                                item.filledIcon
                            } else {
                                item.outlinedIcon
                            },
                            contentDescription = "Icon",
                        )
                    },
                    alwaysShowLabel = false
                )
            }
        }
        Scaffold {
            Box(modifier = Modifier.padding(it)) {
                navigator.lastItem.Content()
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun ExpandedScreen() {
    val navigator = LocalNavigator.currentOrThrow
    PermanentNavigationDrawer(
        modifier = Modifier.padding(start = 10.dp),
        content = {
            Scaffold {
                Box(modifier = Modifier.padding(it)) {
                    navigator.lastItem.Content()
                }
            }
        },
        drawerContent = {
            PermanentDrawerSheet(modifier = Modifier.width(240.dp)) {
                ScreenList.screens.forEach { item ->
                    val selected = item.screen == navigator.lastItem
                    NavigationDrawerItem(
                        selected = selected,
                        onClick = {
                            navigator.push(item.screen)
                        },
                        label = {
                            Text(
                                text = item.name,
                                fontWeight = FontWeight.SemiBold,
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = if (selected) {
                                    item.filledIcon
                                } else {
                                    item.outlinedIcon
                                },
                                contentDescription = "Icon",
                            )
                        }
                    )
                }
            }
        }
    )
}

internal expect fun openUrl(url: String?)
internal expect fun generateImageLoader(): ImageLoader