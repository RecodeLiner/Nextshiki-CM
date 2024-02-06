package com.rcl.nextshiki.base.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.rcl.nextshiki.base.search.mainsearchscreen.MainSearchComponent
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.base.search.searchedelementscreen.SearchedElementComponent
import kotlinx.serialization.Serializable

class SearchComponent(context: ComponentContext) : ComponentContext by context {
    private val navigator = StackNavigation<SearchConfiguration>()

    val childStack = childStack(
        source = navigator,
        serializer = SearchConfiguration.serializer(),
        initialConfiguration = SearchConfiguration.MainSearchScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    fun onBack() {
        navigator.pop()
    }

    private fun createChild(
        config: SearchConfiguration,
        context: ComponentContext,
    ): SearchLevelChild {
        return when (config) {
            is SearchConfiguration.MainSearchScreen -> SearchLevelChild.MainSearchScreen(
                MainSearchComponent(
                    context = context,
                    navigator = navigator
                )
            )

            is SearchConfiguration.SearchedElementScreen -> SearchLevelChild.SearchedElementScreen(
                SearchedElementComponent(
                    id = config.id,
                    type = config.type,
                    context = context,
                    navigator = navigator
                )
            )
        }
    }

    sealed class SearchLevelChild {
        data class MainSearchScreen(val component: MainSearchComponent) : SearchLevelChild()
        data class SearchedElementScreen(val component: SearchedElementComponent) : SearchLevelChild()
    }

    @Serializable
    sealed interface SearchConfiguration {
        @Serializable
        data object MainSearchScreen : SearchConfiguration

        @Serializable
        data class SearchedElementScreen(val id: Int, val type: SearchType) : SearchConfiguration
    }
}