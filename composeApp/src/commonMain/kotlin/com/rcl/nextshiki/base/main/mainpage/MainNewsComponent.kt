package com.rcl.nextshiki.base.main.mainpage

import Nextshiki.composeApp.BuildConfig
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rcl.nextshiki.base.main.mainpage.subelements.CardElement
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.topics.HotTopics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainNewsComponent(context: ComponentContext) : ComponentContext by context, IMain, KoinComponent {
    private val _cardElement = MutableValue(CardElement())
    override val cardElement: Value<CardElement> = _cardElement
    override val topicsList = mutableListOf<HotTopics>()
    private val coroutine = CoroutineScope(Default)
    private val ktorRepository: KtorRepository by inject()

    init {
        lifecycle.doOnCreate {
            coroutine.launch {
                val cardModel = ktorRepository.getCalendar()[0]
                _cardElement.value = CardElement(
                    name = cardModel.anime!!.name!!,
                    imageLink = BuildConfig.DOMAIN + cardModel.anime.image!!.preview!!,
                    nextEpisodeAt = cardModel.nextEpisodeAt!!
                )
                val list = ktorRepository.getHotTopics()
                topicsList.addAll(list)
            }
        }
    }
}