package com.rcl.nextshiki.base.main.newspage

import Nextshiki.composeApp.BuildConfig.DOMAIN
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.pop
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.base.RootComponent.TopLevelConfiguration.SearchScreenConfiguration.SearchedElementScreen
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.models.topics.HotTopics

class NewsPageComponent(
    context: ComponentContext,
    val topic: HotTopics,
    val navigator: StackNavigation<RootComponent.TopLevelConfiguration>
) : ComponentContext by context {
    fun navBack() {
        navigator.pop()
    }

    fun extractLink(link: String?): String? {
        link ?: return null
        val regex = Regex("""(?<=src=")(.*?)(?=")""")
        val list = regex.find(link)
        list ?: return null
        return if (list.value.startsWith("//")) {
            "https:${list.value}"
        } else if (list.value.startsWith("/")) {
            "$DOMAIN${list.value}"
        } else {
            list.value
        }
    }

    fun navigateTo(id: String,contentType: SearchType) {
        navigator.bringToFront(
            SearchedElementScreen(
                id = id,
                contentType = contentType
            )
        )
    }

    fun removeExtra(string: String): String {
        val regex = Regex("(<br class=\"br\">)\\1+")
        return string.replace(regex, "$1")
    }
}