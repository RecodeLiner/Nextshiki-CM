package com.rcl.nextshiki.base.main.newspage

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.pop
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.base.RootComponent.TopLevelConfiguration.SearchScreenConfiguration.SearchedElementScreen
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.models.topics.HotTopics

@Stable
class NewsPageComponent(
    context: ComponentContext,
    val topic: HotTopics,
    val navigator: StackNavigation<RootComponent.TopLevelConfiguration>
) : ComponentContext by context {
    fun navBack() {
        navigator.pop()
    }

    fun navigateTo(id: String, contentType: SearchType) {
        navigator.bringToFront(
            SearchedElementScreen(
                id = id,
                contentType = contentType
            )
        )
    }
}
