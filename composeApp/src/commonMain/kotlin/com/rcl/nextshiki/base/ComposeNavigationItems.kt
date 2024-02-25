package com.rcl.nextshiki.base

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
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.moko.MR.strings.bottom_main
import com.rcl.moko.MR.strings.bottom_profile
import com.rcl.moko.MR.strings.bottom_search
import com.rcl.nextshiki.base.main.mainpage.MainComponentScreen
import com.rcl.nextshiki.base.profile.ProfileComponentScreen
import com.rcl.nextshiki.base.search.SearchComponentScreen
import dev.icerock.moko.resources.compose.stringResource

val screens = listOf(
    Routes(
        name = bottom_main,
        configuration = RootComponent.TopLevelConfiguration.MainScreen,
        outlinedIcon = Icons.Outlined.Home,
        filledIcon = Icons.Filled.Home
    ),
    Routes(
        name = bottom_search,
        configuration = RootComponent.TopLevelConfiguration.SearchScreen,
        outlinedIcon = Icons.Outlined.Search,
        filledIcon = Icons.Filled.Search
    ),
    Routes(
        name = bottom_profile,
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
            initBox(paddings, rootComponent)
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background
            ) {
                screens.forEach { item ->
                    val selected = stack.active.configuration == item.configuration
                    NavigationBarItem(
                        //enabled = navEnabled.value,
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
                    //enabled = navEnabled.value,
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
                    val selected = stack.active.configuration == item.configuration
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
            }
        }
    )
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun initBox(
    paddings: PaddingValues,
    rootComponent: RootComponent
) {
    Box(modifier = Modifier.padding(paddings)) {
        Children(
            stack = rootComponent.childStack,
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

//val navEnabled = mutableStateOf(true)