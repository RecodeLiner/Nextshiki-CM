package com.rcl.nextshiki

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.rcl.nextshiki.Koin.setupKoin
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.base.Routes
import com.rcl.nextshiki.base.main.MainComponentScreen
import com.rcl.nextshiki.base.profile.ProfileComponentScreen
import com.rcl.nextshiki.base.search.SearchComponentScreen
import com.rcl.nextshiki.models.currentuser.TokenModel
import com.rcl.nextshiki.theme.Theme.AppTheme
import com.russhwolf.settings.Settings
import com.seiko.imageloader.ImageLoader
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.github.skeptick.libres.LibresSettings


fun setupBaseApp() {
    setupNapier()
    setupKoin()
}

@Composable
fun App(rootComponent: RootComponent, seedColor: Color = MaterialTheme.colorScheme.primary) = AppTheme(
    seedColor = seedColor
) {
    setupBaseApp()
    setupUI(rootComponent)
}

internal fun setupNapier() {
    Napier.base(DebugAntilog())
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun setupUI(rootComponent: RootComponent) {
    val widthSizeClass = calculateWindowSizeClass().widthSizeClass

    when (widthSizeClass) {
        Compact -> {
            navBar(rootComponent)
        }

        Medium -> {
            mediumScreen(rootComponent)
        }

        Expanded -> {
            expandedScreen(rootComponent)
        }
    }
}

val screens = listOf(
    Routes(
        name = MainRes.string.bottom_main,
        configuration = RootComponent.TopLevelConfiguration.MainScreen,
        outlinedIcon = Icons.Outlined.Home,
        filledIcon = Icons.Filled.Home
    ),
    Routes(
        name = MainRes.string.bottom_search,
        configuration = RootComponent.TopLevelConfiguration.SearchScreen,
        outlinedIcon = Icons.Outlined.Search,
        filledIcon = Icons.Filled.Search
    ),
    Routes(
        name = MainRes.string.bottom_profile,
        configuration = RootComponent.TopLevelConfiguration.ProfileScreen,
        outlinedIcon = Icons.Outlined.AccountCircle,
        filledIcon = Icons.Filled.AccountCircle
    )
)

@Composable
fun navBar(rootComponent: RootComponent) {
    val stack by rootComponent.childStack.subscribeAsState()
    Scaffold(
        content = { paddings ->
            initBox(paddings, rootComponent, stack)
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background
            ) {
                screens.forEach { item ->
                    val selected = stack.active.configuration == item.configuration
                    NavigationBarItem(
                        enabled = navEnabled.value,
                        selected = selected,
                        onClick = {
                            rootComponent.navigateTo(item.configuration)
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

@Composable
fun mediumScreen(rootComponent: RootComponent) {
    val stack by rootComponent.childStack.subscribeAsState()
    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail {
            screens.forEach { item ->
                val selected = stack.active.configuration == item.configuration
                NavigationRailItem(
                    enabled = navEnabled.value,
                    selected = selected,
                    onClick = {
                        rootComponent.navigateTo(item.configuration)
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
        Scaffold { paddings ->
            initBox(paddings, rootComponent, stack)
        }
    }
}

@Composable
fun expandedScreen(rootComponent: RootComponent) {
    val stack by rootComponent.childStack.subscribeAsState()
    PermanentNavigationDrawer(
        modifier = Modifier.padding(start = 10.dp),
        content = {
            Scaffold { paddings ->
                initBox(paddings, rootComponent, stack)
            }
        },
        drawerContent = {
            PermanentDrawerSheet(modifier = Modifier.width(240.dp)) {
                screens.forEach { item ->
                    val selected = stack.active.configuration == item.configuration
                    NavigationDrawerItem(
                        selected = selected,
                        onClick = {
                            rootComponent.navigateTo(item.configuration)
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

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun initBox(
    paddings: PaddingValues,
    rootComponent: RootComponent,
    stack: ChildStack<RootComponent.TopLevelConfiguration, RootComponent.TopLevelChild>
) {
    Box(modifier = Modifier.padding(paddings)) {
        Children(
            stack = stack,
            animation = predictiveBackAnimation(
                backHandler = rootComponent::backHandler.get(),
                onBack = {
                    rootComponent.onBack()
                },
            ),
        ) { topLevelChild ->
            when (val instance = topLevelChild.instance) {
                is RootComponent.TopLevelChild.MainScreen -> MainComponentScreen(instance.component)
                is RootComponent.TopLevelChild.SearchScreen -> SearchComponentScreen(instance.component)
                is RootComponent.TopLevelChild.ProfileScreen -> ProfileComponentScreen(instance.component)
            }
        }
    }
}

val link = mutableStateOf<String?>(null)

val settings: Settings = Settings()

val navEnabled = mutableStateOf(true)

fun String.upper() = replaceFirstChar(Char::titlecase)
fun String.supper() = replaceFirstChar(Char::lowercase)

fun setupLanguage(code: String?) {
    LibresSettings.languageCode = code
}

fun clearLanguage() {
    LibresSettings.languageCode = null
}

internal expect fun openUrl(url: String?)
internal expect fun generateImageLoader(): ImageLoader

internal expect suspend fun getToken(isFirst: Boolean, code: String): TokenModel
internal expect fun copyToClipboard(text: String)