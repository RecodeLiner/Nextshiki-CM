package com.rcl.nextshiki.compose

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.rcl.nextshiki.SharedRes.strings.bottom_main
import com.rcl.nextshiki.SharedRes.strings.bottom_profile
import com.rcl.nextshiki.SharedRes.strings.bottom_search
import com.rcl.nextshiki.components.RootComponent
import com.rcl.nextshiki.compose.screens.MainNewsScreen
import com.rcl.nextshiki.compose.screens.MainProfileScreen
import com.rcl.nextshiki.compose.screens.MainSearchScreen
import com.rcl.nextshiki.compose.screens.NewsPageScreen
import com.rcl.nextshiki.compose.screens.ProfileHistoryScreen
import com.rcl.nextshiki.compose.screens.SearchedElementScreen
import com.rcl.nextshiki.compose.screens.SettingsScreen
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource

val screens = listOf(
    Routes(
        name = bottom_main,
        configuration = RootComponent.TopLevelConfiguration.MainScreenConfiguration.MainNewsConfiguration,
        outlinedIcon = Icons.Outlined.Home,
        filledIcon = Icons.Filled.Home
    ),
    Routes(
        name = bottom_search,
        configuration = RootComponent.TopLevelConfiguration.SearchScreenConfiguration.MainSearchConfiguration,
        outlinedIcon = Icons.Outlined.Search,
        filledIcon = Icons.Filled.Search
    ),
    Routes(
        name = bottom_profile,
        configuration = RootComponent.TopLevelConfiguration.ProfileScreenConfiguration.MainProfileConfiguration,
        outlinedIcon = Icons.Outlined.AccountCircle,
        filledIcon = Icons.Filled.AccountCircle
    )
)

@Composable
fun NavBar(rootComponent: RootComponent) {
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
                                text = stringResource(item.name),
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

@Composable
fun MediumScreen(rootComponent: RootComponent) {
    val stack by rootComponent.childStack.subscribeAsState()
    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail {
            Spacer(modifier = Modifier.weight(1f))
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
                            text = stringResource(item.name),
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
            Spacer(modifier = Modifier.weight(1f))
        }
        Scaffold { paddings ->
            initBox(paddings, rootComponent)
        }
    }
}

@Composable
fun ExpandedScreen(rootComponent: RootComponent) {
    val stack by rootComponent.childStack.subscribeAsState()
    PermanentNavigationDrawer(
        modifier = Modifier.padding(start = 10.dp),
        content = {
            Scaffold { paddings ->
                initBox(paddings, rootComponent)
            }
        },
        drawerContent = {
            PermanentDrawerSheet(modifier = Modifier.width(120.dp)) {
                Spacer(modifier = Modifier.weight(1f))
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
                                text = stringResource(item.name),
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
                Spacer(modifier = Modifier.weight(1f))
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
                    is RootComponent.TopLevelChild.MainScreenChild.MainNewsChild -> ProvideAnimatedVisibilityScope {
                        MainNewsScreen(
                            instance.component
                        )
                    }

                    is RootComponent.TopLevelChild.MainScreenChild.NewsPageChild -> ProvideAnimatedVisibilityScope {
                        NewsPageScreen(
                            instance.component
                        )
                    }

                    is RootComponent.TopLevelChild.SearchScreenChild.MainSearchScreenChild -> ProvideAnimatedVisibilityScope {
                        MainSearchScreen(
                            instance.component
                        )
                    }

                    is RootComponent.TopLevelChild.SearchScreenChild.SearchedElementChild -> ProvideAnimatedVisibilityScope {
                        SearchedElementScreen(
                            instance.component
                        )
                    }

                    is RootComponent.TopLevelChild.ProfileScreenChild.MainProfileScreenChild ->
                        MainProfileScreen(
                            instance.component
                        )

                    is RootComponent.TopLevelChild.ProfileScreenChild.ProfileHistoryScreenChild ->
                        ProfileHistoryScreen(
                            instance.component
                        )

                    is RootComponent.TopLevelChild.ProfileScreenChild.SettingsScreenChild ->
                        SettingsScreen(
                            instance.component
                        )

                    else -> {}
                }
            }
        }
    }
}

data class Routes(
    val name: StringResource,
    val configuration: RootComponent.TopLevelConfiguration,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector,
)
