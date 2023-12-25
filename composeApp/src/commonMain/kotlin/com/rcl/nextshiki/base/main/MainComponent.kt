package com.rcl.nextshiki.base.main

import Nextshiki.composeApp.BuildConfig
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rcl.nextshiki.base.coroutineScope
import com.rcl.nextshiki.base.main.subelements.CardElement
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.koin
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainComponent(
    context: ComponentContext,
    mainContext: CoroutineContext
) : ComponentContext by context, IMain {
    private val _cardElement = MutableValue(CardElement())
    override val cardElement: Value<CardElement> = _cardElement
    private val coroutine = coroutineScope(mainContext + SupervisorJob())

    init {
        lifecycle.doOnCreate {
            coroutine.launch {
                val cardModel = koin.get<KtorRepository>().getCalendar()[0]
                _cardElement.value = CardElement(
                    name = cardModel.anime!!.name!!,
                    imageLink = BuildConfig.DOMAIN + cardModel.anime.image!!.preview!!,
                    nextEpisodeAt = cardModel.nextEpisodeAt!!
                )
            }
        }
    }
}