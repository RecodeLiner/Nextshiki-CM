package com.rcl.nextshiki.components

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.rcl.nextshiki.features.di.IDIComponentContext
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: IDIComponentContext,
): IDIComponentContext by componentContext {
    private val navigation = StackNavigation<ScreenConfig>()

    fun navigateBack() {
        navigation.pop()
    }

    val childStack = childStack(
        source = navigation,
        initialConfiguration = ScreenConfig.Home,
        handleBackButton = true,
        childFactory = ::createChild,
        serializer = ScreenConfig.serializer()
    )

    private fun createChild(config: ScreenConfig, context: IDIComponentContext): ComponentChild {
        return when (config) {
            ScreenConfig.Home -> ComponentChild.HomeChild(
                HomeComponent(context)
            )
            ScreenConfig.Search -> ComponentChild.SearchChild(
                SearchComponent(context)
            )
            ScreenConfig.Profile -> ComponentChild.ProfileChild(
                ProfileComponent(context)
            )
        }
    }

    sealed class ComponentChild {
        data class HomeChild(val components: HomeComponent) : ComponentChild()
        data class SearchChild(val components: SearchComponent) : ComponentChild()
        data class ProfileChild(val component: ProfileComponent) : ComponentChild()
    }

    @Serializable
    sealed interface ScreenConfig {
        @Serializable
        data object Home : ScreenConfig

        @Serializable
        data object Search : ScreenConfig

        @Serializable
        data object Profile : ScreenConfig
    }

}