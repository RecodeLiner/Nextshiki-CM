package com.rcl.nextshiki.base.main.mainpage

import Nextshiki.composeApp.BuildConfig.DOMAIN
import androidx.compose.runtime.mutableStateListOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rcl.nextshiki.base.main.mainpage.subelements.CardElement
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.topics.ForumType
import com.rcl.nextshiki.models.topics.HotTopics
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainNewsComponent(context: ComponentContext) : ComponentContext by context, KoinComponent {
    private val ktorRepository: KtorRepository by inject()

    val topicsList = mutableStateListOf<HotTopics>()
    val cardList = mutableStateListOf<CardElement>()

    private val coroutine = CoroutineScope(Dispatchers.IO)


    init {
        lifecycle.doOnCreate {
            coroutine.launch {
                    val calendarList = ktorRepository.getCalendar()
                    val newsList = ktorRepository.getTopics(forum = ForumType.news)
                    withContext(Dispatchers.Main) {
                        calendarList.forEach { model ->
                            model.anime?.id?.let { id ->
                                model.anime.name?.let { animeName ->
                                    model.nextEpisodeAt?.let { nextEpisodeAt ->
                                        CardElement(
                                            id = id,
                                            name = animeName,
                                            imageLink = DOMAIN + model.anime.image!!.preview!!,
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
                        topicsList.addAll(newsList)
                }
            }
        }
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