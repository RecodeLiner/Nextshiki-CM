package com.rcl.nextshiki.components.newscomponent.mainpage

import Nextshiki.resources.BuildConfig
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rcl.nextshiki.components.IWebUri
import com.rcl.nextshiki.components.RootComponent
import com.rcl.nextshiki.components.RootComponent.TopLevelConfiguration.MainScreenConfiguration.NewsPageConfiguration
import com.rcl.nextshiki.components.RootComponent.TopLevelConfiguration.SearchScreenConfiguration.SearchedElementConfiguration
import com.rcl.nextshiki.components.newscomponent.mainpage.subelements.CardElement
import com.rcl.nextshiki.di.KtorRepository
import com.rcl.nextshiki.di.language.LanguageRepo
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.SearchType
import com.rcl.nextshiki.models.topics.ForumType
import com.rcl.nextshiki.models.topics.HotTopics
import com.rcl.nextshiki.utils.getValidUrlByLink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(DelicateDecomposeApi::class)
class MainNewsComponent(
    context: ComponentContext,
    val navigator: StackNavigation<RootComponent.TopLevelConfiguration>
) : ComponentContext by context, IWebUri {
    override val currentLink = MutableValue("${BuildConfig.DOMAIN}/forum/news")

    val vm = instanceKeeper.getOrCreate { MainNewsViewModel() }

    class MainNewsViewModel() : InstanceKeeper.Instance, KoinComponent {
        private val ktorRepository: KtorRepository by inject()
        val languageRepo: LanguageRepo by inject()
        val topicsList = MutableValue<List<HotTopics>>(emptyList())
        val cardList = MutableValue<List<CardElement>>(emptyList())
        private val coroutine = CoroutineScope(Dispatchers.IO)

        fun getCurrentTime(
            time: String,
            timeInstance: Instant = Clock.System.now()
        ): Int {
            val eventInstant = Instant.parse(time)
            val duration = eventInstant.minus(timeInstance)
            return duration.inWholeHours.toInt()
        }

        fun onCreate() {
            coroutine.launch {
                val calendarList = ktorRepository.getCalendar()
                val newsList = ktorRepository.getTopics(forum = ForumType.News)

                withContext(Dispatchers.Main) {
                    calendarList.forEach { model ->
                        model.anime?.id?.let { id ->
                            model.anime!!.name?.let { animeName ->
                                model.nextEpisodeAt?.let { nextEpisodeAt ->
                                    model.anime!!.russian?.let { russian ->
                                        CardElement(
                                            id = id,
                                            name = animeName,
                                            russian = russian,
                                            imageLink = getValidUrlByLink(
                                                model.anime!!.image?.preview!!,
                                                domain = BuildConfig.DOMAIN
                                            ),
                                            nextEpisodeAt = nextEpisodeAt
                                        )
                                    }
                                }
                            }
                        }?.let { card ->
                            cardList.update {
                                it + card
                            }
                        }
                    }
                    topicsList.update {
                        it + newsList
                    }
                }
            }
        }
    }

    fun navigateToNews(topic: HotTopics) {
        navigator.pushNew(NewsPageConfiguration(topic))
    }

    fun navigateToCard(id: Int, contentType: SearchType = SearchType.Anime) {
        navigator.pushNew(
            SearchedElementConfiguration(
                contentType = contentType,
                cardModel = SearchCardModel(id = id, searchType = contentType)
            )
        )
    }


    init {
        lifecycle.doOnCreate {
            vm.onCreate()
        }
    }
}
