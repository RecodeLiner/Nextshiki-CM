package com.rcl.nextshiki.base.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.rcl.nextshiki.base.WebResourceConstitute
import com.rcl.nextshiki.base.main.mainpage.MainNewsComponent
import com.rcl.nextshiki.base.main.newspage.NewsPageComponent
import kotlinx.serialization.Serializable

class MainComponent(context: ComponentContext) : ComponentContext by context, WebResourceConstitute {
    private val navigator = StackNavigation<NewsConfiguration>()

    private fun navigateTo(config: NewsConfiguration) {
        navigator.bringToFront(config)
    }

    val childStack = childStack(
        source = navigator,
        serializer = NewsConfiguration.serializer(),
        initialConfiguration = NewsConfiguration.MainNewsScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    fun onBack() {
        navigator.pop()
    }

    private fun createChild(
        config: NewsConfiguration,
        context: ComponentContext
    ): NewsLevelChild {
        return when (config) {
            NewsConfiguration.MainNewsScreen -> NewsLevelChild.MainNewsScreen(
                MainNewsComponent(context = context)
            )

            NewsConfiguration.NewsPageScreen -> NewsLevelChild.NewsPageScreen(
                NewsPageComponent(context = context)
            )
        }
    }

    sealed class NewsLevelChild {
        data class MainNewsScreen(val component: MainNewsComponent) : NewsLevelChild()
        data class NewsPageScreen(val component: NewsPageComponent) : NewsLevelChild()
    }

    @Serializable
    sealed interface NewsConfiguration {
        @Serializable
        data object MainNewsScreen : NewsConfiguration

        @Serializable
        data object NewsPageScreen : NewsConfiguration
    }

    override val webUri = null
}