package com.rcl.nextshiki.base.main.mainpage

import Nextshiki.composeApp.BuildConfig
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.base.RootComponent.TopLevelConfiguration.MainScreenConfiguration.NewsPage
import com.rcl.nextshiki.base.RootComponent.TopLevelConfiguration.SearchScreenConfiguration.SearchedElementScreen
import com.rcl.nextshiki.base.main.mainpage.subelements.CardElement
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.elements.IWebUri
import com.rcl.nextshiki.elements.getValidUrlByLink
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.topics.ForumType
import com.rcl.nextshiki.models.topics.HotTopics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(DelicateDecomposeApi::class)
@Stable
class MainNewsComponent(
    context: ComponentContext,
    val navigator: StackNavigation<RootComponent.TopLevelConfiguration>
) : ComponentContext by context, KoinComponent, IWebUri {
    override val currentLinkFlow = MutableValue("${BuildConfig.DOMAIN}/forum/news")

    private val ktorRepository: KtorRepository by inject()

    val topicsList = mutableStateListOf<HotTopics>()
    val cardList = mutableStateListOf<CardElement>()

    private val coroutine = CoroutineScope(Dispatchers.IO)

    fun navigateToNews(topic: HotTopics) {
        navigator.pushNew(NewsPage(topic))
    }

    fun navigateToCard(id: Int, contentType: SearchType = SearchType.Anime) {
        navigator.pushNew(SearchedElementScreen(contentType = contentType, cardModel = SearchCardModel(id = id)))
    }


    init {
        lifecycle.doOnCreate {
            coroutine.launch {
                val calendarList = ktorRepository.getCalendar()
                val newsList = ktorRepository.getTopics(forum = ForumType.News)
                withContext(Dispatchers.Main) {
                    calendarList.forEach { model ->
                        model.anime?.id?.let { id ->
                            model.anime.name?.let { animeName ->
                                model.nextEpisodeAt?.let { nextEpisodeAt ->
                                    model.anime.russian?.let { russian ->
                                        CardElement(
                                            id = id,
                                            name = animeName,
                                            russian = russian,
                                            imageLink = getValidUrlByLink(model.anime.image?.preview!!),
                                            nextEpisodeAt = nextEpisodeAt
                                        )
                                    }
                                }
                            }
                        }?.let {
                            cardList.add(
                                it
                            )
                        }
                    }
                    topicsList.addAll(newsList)
                }
            }
        }
    }
}
