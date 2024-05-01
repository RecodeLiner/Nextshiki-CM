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
import io.github.aakira.napier.Napier

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

    fun navigateByLink(link: String) {
        val list = link.split("/")
        when (list[3]) {
            "animes" -> {
                navigator.bringToFront(
                    SearchedElementScreen(
                        list[4].split("-")[0],
                        SearchType.Anime
                    )
                )
            }

            "mangas" -> {
                navigator.bringToFront(
                    SearchedElementScreen(
                        list[4].split("-")[0],
                        SearchType.Manga
                    )
                )
            }

            "ranobe" -> {
                navigator.bringToFront(
                    SearchedElementScreen(
                        list[4].split("-")[0],
                        SearchType.Ranobe
                    )
                )
            }

            "people" -> {
                navigator.bringToFront(
                    SearchedElementScreen(
                        list[4].split("-")[0],
                        SearchType.People
                    )
                )
            }

            "users" -> {
                navigator.bringToFront(
                    SearchedElementScreen(
                        list[4].split("-")[0],
                        SearchType.Users
                    )
                )
            }

            "characters" -> {
                navigator.bringToFront(
                    SearchedElementScreen(
                        list[4].split("-")[0],
                        SearchType.Characters
                    )
                )
            }

            else -> {
                Napier.i("uri - $link, part - ${list[3]}")
            }
        }
    }
}