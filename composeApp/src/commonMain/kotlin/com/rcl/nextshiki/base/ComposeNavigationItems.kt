package com.rcl.nextshiki.base

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.mr.SharedRes.strings.bottom_main
import com.rcl.mr.SharedRes.strings.bottom_profile
import com.rcl.mr.SharedRes.strings.bottom_search
import com.rcl.nextshiki.base.main.mainpage.MainNewsComponentScreen
import com.rcl.nextshiki.base.main.newspage.NewsPageScreen
import com.rcl.nextshiki.base.profile.historypage.ProfileHistoryScreen
import com.rcl.nextshiki.base.profile.mainprofile.MainProfileComponentScreen
import com.rcl.nextshiki.base.profile.settings.SettingsComponentScreen
import com.rcl.nextshiki.base.search.mainsearchscreen.MainSearchComponentScreen
import com.rcl.nextshiki.base.search.searchedelementscreen.SearchedElementComponentScreen
import com.rcl.nextshiki.elements.ProvideAnimatedVisibilityScope
import com.rcl.nextshiki.elements.SharedTransitionScopeProvider
import com.rcl.nextshiki.locale.CustomLocale.getLocalizableString
import kotlinx.collections.immutable.persistentListOf

val screens = persistentListOf(
    Routes(
        name = bottom_main,
        configuration = RootComponent.TopLevelConfiguration.MainScreenConfiguration.MainNews,
        outlinedIcon = Icons.Outlined.Home,
        filledIcon = Icons.Filled.Home
    ),
    Routes(
        name = bottom_search,
        configuration = RootComponent.TopLevelConfiguration.SearchScreenConfiguration.MainSearchScreen,
        outlinedIcon = Icons.Outlined.Search,
        filledIcon = Icons.Filled.Search
    ),
    Routes(
        name = bottom_profile,
        configuration = RootComponent.TopLevelConfiguration.ProfileScreenConfiguration.MainProfileScreen,
        outlinedIcon = Icons.Outlined.AccountCircle,
        filledIcon = Icons.Filled.AccountCircle
    )
)

@Composable
fun navBar(rootComponent: RootComponent) {
    val stack by rootComponent.childStack.subscribeAsState()
    Scaffold(
        content = { paddings ->
            initBox(paddings, rootComponent)
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background
            ) {
                screens.forEach { item ->
                    val selected =
                        stack.active.configuration.topLevelType == item.configuration.topLevelType
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            rootComponent.navigateTo(item.configuration)
                        },
                        label = {
                            Text(
                                text = item.name.getLocalizableString(),
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
                val selected =
                    stack.active.configuration.topLevelType == item.configuration.topLevelType
                NavigationRailItem(
                    selected = selected,
                    onClick = {
                        rootComponent.navigateTo(item.configuration)
                    },
                    label = {
                        Text(
                            text = item.name.getLocalizableString(),
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
            initBox(paddings, rootComponent)
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
                initBox(paddings, rootComponent)
            }
        },
        drawerContent = {
            PermanentDrawerSheet(modifier = Modifier.width(240.dp)) {
                screens.forEach { item ->
                    val selected =
                        stack.active.configuration.topLevelType == item.configuration.topLevelType
                    NavigationDrawerItem(
                        selected = selected,
                        onClick = {
                            rootComponent.navigateTo(item.configuration)
                        },
                        label = {
                            Text(
                                text = item.name.getLocalizableString(),
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

@OptIn(ExperimentalDecomposeApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun initBox(
    paddings: PaddingValues,
    rootComponent: RootComponent
) {
    Box(modifier = Modifier.padding(paddings)) {
        SharedTransitionScopeProvider {
            val stack by rootComponent.childStack.subscribeAsState()
            ChildStack(
                stack = stack,
                animation = stackAnimation(
                    animator = fade() + scale(),
                    predictiveBackParams = {
                        PredictiveBackParams(
                            backHandler = rootComponent.backHandler,
                            onBack = rootComponent::onBack,
                        )
                    },
                ),
            ) { topLevelChild ->
                when (val instance = topLevelChild.instance) {
                    is RootComponent.TopLevelChild.MainScreen.MainNews -> ProvideAnimatedVisibilityScope {
                        MainNewsComponentScreen(
                            instance.component
                        )
                    }

                    is RootComponent.TopLevelChild.MainScreen.NewsPage -> ProvideAnimatedVisibilityScope {
                        NewsPageScreen(
                            instance.component
                        )
                    }

                    is RootComponent.TopLevelChild.SearchScreen.MainSearchScreen -> ProvideAnimatedVisibilityScope {
                        MainSearchComponentScreen(
                            instance.component
                        )
                    }

                    is RootComponent.TopLevelChild.SearchScreen.SearchedElementScreen -> ProvideAnimatedVisibilityScope {
                        SearchedElementComponentScreen(
                            instance.component
                        )
                    }

                    is RootComponent.TopLevelChild.ProfileScreen.MainProfileScreen -> MainProfileComponentScreen(
                        instance.component
                    )

                    is RootComponent.TopLevelChild.ProfileScreen.ProfileHistoryScreen -> ProfileHistoryScreen(
                        instance.component
                    )

                    is RootComponent.TopLevelChild.ProfileScreen.SettingsScreen -> SettingsComponentScreen(
                        instance.component
                    )

                    else -> {}
                }
            }
        }
    }
}
