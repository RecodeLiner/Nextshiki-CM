package com.rcl.nextshiki.base.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.rcl.nextshiki.base.search.mainsearchscreen.MainSearchComponent
import com.rcl.nextshiki.base.search.searchedelementscreen.SearchedElementComponent
import com.rcl.nextshiki.models.searchobject.SearchDataModel
import kotlinx.coroutines.MainScope
import kotlinx.serialization.Serializable

class SearchComponent(context: ComponentContext) : ComponentContext by context {
    private val navigator = StackNavigation<SearchConfiguration>()

    fun navigateTo(config: SearchConfiguration) {
        navigator.bringToFront(config)
    }

    val childStack = childStack(
        source = navigator,
        serializer = SearchConfiguration.serializer(),
        initialConfiguration = SearchConfiguration.MainSearchScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    fun onBack(){
        navigator.pop()
    }
    private fun createChild(
        config: SearchConfiguration,
        context: ComponentContext,
        data: SearchDataModel = SearchDataModel()
    ): SearchLevelChild {
        return when (config) {
            SearchConfiguration.MainSearchScreen -> SearchLevelChild.MainSearchScreen(
                MainSearchComponent(context = context)
            )
            SearchConfiguration.SearchedElementScreen -> SearchLevelChild.SearchedElementScreen(
                SearchedElementComponent(data = data, context = context, searchContext = MainScope().coroutineContext )
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
        data object SearchedElementScreen : SearchConfiguration
    }
}