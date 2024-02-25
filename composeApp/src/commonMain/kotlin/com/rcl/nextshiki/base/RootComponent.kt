package com.rcl.nextshiki.base

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.rcl.nextshiki.base.main.mainpage.MainComponent
import com.rcl.nextshiki.base.profile.ProfileComponent
import com.rcl.nextshiki.base.search.SearchComponent
import kotlinx.serialization.Serializable

class RootComponent(context: ComponentContext) : ComponentContext by context, WebResourceConstitute {
    private val navigator = StackNavigation<TopLevelConfiguration>()

    val childStack = childStack(
        source = navigator,
        serializer = TopLevelConfiguration.serializer(),
        initialConfiguration = TopLevelConfiguration.MainScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    fun navigateTo(config: TopLevelConfiguration) {
        navigator.bringToFront(config)
    }

    fun onBack() {
        navigator.pop()
    }

    private fun createChild(
        config: TopLevelConfiguration,
        context: ComponentContext,
    ): TopLevelChild {
        return when (config) {
            TopLevelConfiguration.MainScreen -> TopLevelChild.MainScreen(
                MainComponent(context = context)
            )
            TopLevelConfiguration.SearchScreen -> TopLevelChild.SearchScreen(
                SearchComponent(context = context)
            )
            TopLevelConfiguration.ProfileScreen -> TopLevelChild.ProfileScreen(
                ProfileComponent(context = context)
            )
        }
    }

    sealed class TopLevelChild {
        data class MainScreen(val component: MainComponent) : TopLevelChild()
        data class SearchScreen(val component: SearchComponent) : TopLevelChild()
        data class ProfileScreen(val component: ProfileComponent) : TopLevelChild()
    }

    @Serializable
    sealed interface TopLevelConfiguration {

        @Serializable
        data object MainScreen : TopLevelConfiguration

        @Serializable
        data object SearchScreen : TopLevelConfiguration

        @Serializable
        data object ProfileScreen : TopLevelConfiguration
    }

    override val webUri: String?
        get() = (childStack.active.instance as? WebResourceConstitute)?.webUri
}