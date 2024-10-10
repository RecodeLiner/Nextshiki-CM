package com.rcl.nextshiki.components.newscomponent.newspage

import Nextshiki.resources.BuildConfig
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.MutableValue
import com.rcl.nextshiki.components.IWebUri
import com.rcl.nextshiki.components.RootComponent
import com.rcl.nextshiki.components.RootComponent.TopLevelConfiguration.SearchScreenConfiguration.SearchedElementConfiguration
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.SearchType
import com.rcl.nextshiki.models.topics.HotTopics

class NewsPageComponent(
    context: ComponentContext,
    val topic: HotTopics,
    private val navigator: StackNavigation<RootComponent.TopLevelConfiguration>
) : ComponentContext by context, IWebUri {
    fun navBack() {
        navigator.pop()
    }

    fun navigateTo(cardModel: SearchCardModel, contentType: SearchType) {
        navigator.bringToFront(
            SearchedElementConfiguration(
                cardModel = cardModel,
                contentType = contentType
            )
        )
    }

    override val currentLink = MutableValue("${BuildConfig.DOMAIN}/forum/news/${topic.id?:"check"}")
}
