package com.rcl.nextshiki.base.main.mainpage

import Nextshiki.composeApp.BuildConfig
import androidx.compose.runtime.mutableStateListOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rcl.nextshiki.base.main.mainpage.subelements.CardElement
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.topics.ForumType
import com.rcl.nextshiki.models.topics.HotTopics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainNewsComponent(context: ComponentContext) : ComponentContext by context, IMain, KoinComponent {
    private val ktorRepository: KtorRepository by inject()

    override val topicsList = mutableStateListOf<HotTopics>()
    override val cardList = mutableStateListOf<CardElement>()

    private val coroutine = CoroutineScope(Default)


    init {
        lifecycle.doOnCreate {
            coroutine.launch {
                val calendarList = ktorRepository.getCalendar()
                calendarList.forEach { model ->
                    model.anime?.id?.let { id ->
                        model.anime.name?.let { animeName ->
                            model.nextEpisodeAt?.let { nextEpisodeAt ->
                                CardElement(
                                    id = id,
                                    name = animeName,
                                    imageLink = BuildConfig.DOMAIN + model.anime.image!!.preview!!,
                                    nextEpisodeAt = nextEpisodeAt
                                )
                            }
                        }
                    }?.let {
                        cardList.add(
                            it
                        )
                    }
                }
                val newsList = ktorRepository.getTopics(forum = ForumType.news)
                topicsList.addAll(newsList)
            }
        }
    }

    fun extractLink(link: String): String? {
        val regex = Regex("src=\"([^\"]+)\"")
        val matchResult = regex.find(link)
        return matchResult?.groups?.get(1)?.value
    }
}