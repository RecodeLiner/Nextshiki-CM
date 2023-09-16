package com.rcl.nextshiki

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rcl.nextshiki.di.ktor.KtorModel
import com.rcl.nextshiki.screens.ScreenList
import com.rcl.nextshiki.screens.main.MainScreen
import com.rcl.nextshiki.theme.Theme.AppTheme
import com.seiko.imageloader.ImageLoader
import dev.icerock.moko.resources.StringResource
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.Koin
import org.koin.core.context.startKoin

lateinit var koin: Koin

@Composable
internal fun app() = AppTheme {
    setupKoin()
}

@Composable
internal fun setupKoin() {
    koin = startKoin { modules(KtorModel.networkModule) }.koin
    Napier.base(DebugAntilog())
    setupUI()
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun setupUI() {
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass
    Navigator(
        screen = MainScreen()
    ) {
        when (widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                navBar()
            }

            WindowWidthSizeClass.Medium -> {
                mediumScreen()
            }

            WindowWidthSizeClass.Expanded -> {
                expandedScreen()
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun navBar() {
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
                    val selected = item.screen.key == navigator.lastItem.key
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
fun mediumScreen() {
    val navigator = LocalNavigator.currentOrThrow
    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail {
            ScreenList.screens.forEach { item ->
                val selected = item.screen.key == navigator.lastItem.key
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
fun expandedScreen() {
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
                    val selected = item.screen.key == navigator.lastItem.key
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

fun Modifier.noRippleClickable(
    onClick: () -> Unit
) = composed(
    factory = {
        this.then(
            Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onClick() }
            )
        )
    }
)

fun String.upper() = replaceFirstChar(Char::titlecase)

@Composable
internal expect fun getString(id: StringResource, vararg args: List<Any>): String
internal expect fun openUrl(url: String?)
internal expect fun generateImageLoader(): ImageLoader