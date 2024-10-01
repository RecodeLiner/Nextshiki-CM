package com.rcl.nextshiki.base.main.newspage

import Nextshiki.composeApp.BuildConfig
import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.MutableValue
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.base.RootComponent.TopLevelConfiguration.SearchScreenConfiguration.SearchedElementScreen
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.elements.IWebUri
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.topics.HotTopics

@Stable
class NewsPageComponent(
    context: ComponentContext,
    val topic: HotTopics,
    val navigator: StackNavigation<RootComponent.TopLevelConfiguration>
) : ComponentContext by context, IWebUri {
    fun navBack() {
        navigator.pop()
    }

    fun navigateTo(cardModel: SearchCardModel, contentType: SearchType) {
        navigator.bringToFront(
            SearchedElementScreen(
                cardModel = cardModel,
                contentType = contentType
            )
        )
    }
    override val currentLinkFlow = MutableValue("${BuildConfig.DOMAIN}/forum/news/${topic.id?:"check"}")
}
