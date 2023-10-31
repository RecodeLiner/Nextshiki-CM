package com.rcl.nextshiki

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rcl.nextshiki.di.ktor.KtorModel
import com.rcl.nextshiki.models.currentuser.TokenModel
import com.rcl.nextshiki.screens.NavRoutes
import com.rcl.nextshiki.screens.ScreenList
import com.rcl.nextshiki.screens.main.MainScreen
import com.rcl.nextshiki.screens.profile.ProfileScreen
import com.rcl.nextshiki.screens.search.SearchScreen
import com.rcl.nextshiki.theme.Theme.AppTheme
import com.russhwolf.settings.Settings
import com.seiko.imageloader.ImageLoader
import dev.icerock.moko.resources.StringResource
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.Koin
import org.koin.core.context.startKoin

lateinit var koin: Koin
var isInited = false

@Composable
internal fun app() = AppTheme {
    setupNapier()
    setupKoin()
    setupUI()
}

@Composable
internal fun setupKoin() {
    if (!isInited){
        koin = startKoin { modules(KtorModel.networkModule) }.koin
        isInited = true
    }
}

internal fun setupNapier(){
    Napier.base(DebugAntilog())
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun setupUI() {
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass
    Navigator(
        screen = MainScreen()
    ) {
        when (widthSizeClass) {
            Compact -> {
                navBar()
            }

            Medium -> {
                mediumScreen()
            }

            Expanded -> {
                expandedScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
                    val selected = navigator.lastItem.getType() == item.screen.getType()
                    NavigationBarItem(
                        enabled = navEnabled.value,
                        selected = selected,
                        onClick = {
                            navigator.push(item.screen)
                        },
                        label = {
                            Text(
                                text = getString(item.name),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mediumScreen() {
    val navigator = LocalNavigator.currentOrThrow
    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail {
            ScreenList.screens.forEach { item ->
                val selected = navigator.lastItem.getType() == item.screen.getType()
                NavigationRailItem(
                    enabled = navEnabled.value,
                    selected = selected,
                    onClick = {
                        navigator.push(item.screen)
                    },
                    label = {
                        Text(
                            text = getString(item.name),
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

@OptIn(ExperimentalMaterial3Api::class)
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
                    val selected = navigator.lastItem.getType() == item.screen.getType()
                    NavigationDrawerItem(
                        selected = selected,
                        onClick = {
                            navigator.push(item.screen)
                        },
                        label = {
                            Text(
                                text = getString(item.name),
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

val link = mutableStateOf<String?>(null)

val settings: Settings = Settings()

val navEnabled = mutableStateOf(true)

fun String.upper() = replaceFirstChar(Char::titlecase)
fun String.supper() = replaceFirstChar(Char::lowercase)

fun Screen.getType(): NavRoutes {
    return when (this) {
        is MainScreen -> NavRoutes.Main
        is SearchScreen -> NavRoutes.Search
        is ProfileScreen -> NavRoutes.Profile
        else -> NavRoutes.Unknown
    }
}

@Composable
internal expect fun getString(id: StringResource, vararg args: List<Any>): String
internal expect fun openUrl(url: String?)
internal expect fun generateImageLoader(): ImageLoader

internal expect suspend fun getToken(isFirst: Boolean, code: String): TokenModel