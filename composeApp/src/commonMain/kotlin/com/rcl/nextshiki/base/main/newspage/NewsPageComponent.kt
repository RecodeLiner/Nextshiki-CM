package com.rcl.nextshiki.base.main.newspage

import Nextshiki.composeApp.BuildConfig.DOMAIN
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.rcl.nextshiki.base.main.MainComponent
import com.rcl.nextshiki.models.topics.HotTopics

class NewsPageComponent(
    context: ComponentContext,
    val topic: HotTopics,
    val navigator: StackNavigation<MainComponent.NewsConfiguration>
) : ComponentContext by context {
    fun navBack(){
        navigator.pop()
    }

    fun extractLink(link: String?): String? {
        link?: return null
        val regex = Regex("""(?<=src=")(.*?)(?=")""")
        val list = regex.find(link)
        list?: return null
        return if (list.value.startsWith("//"))
        {
            "https:${list.value}"
        } else if (list.value.startsWith("/")) {
            "$DOMAIN${list.value}"
        } else {
            list.value
        }
    }
}