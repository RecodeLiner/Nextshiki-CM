package com.rcl.nextshiki.base.main

import Nextshiki.composeApp.BuildConfig
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rcl.nextshiki.base.main.subelements.CardElement
import com.rcl.nextshiki.di.Koin.getSafeKoin
import com.rcl.nextshiki.di.ktor.KtorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch

class MainComponent(context: ComponentContext) : ComponentContext by context, IMain {
    private val _cardElement = MutableValue(CardElement())
    override val cardElement: Value<CardElement> = _cardElement
    private val coroutine = CoroutineScope(Default)

    init {
        lifecycle.doOnCreate {
            coroutine.launch {
                val cardModel = getSafeKoin().get<KtorRepository>().getCalendar()[0]
                _cardElement.value = CardElement(
                    name = cardModel.anime!!.name!!,
                    imageLink = BuildConfig.DOMAIN + cardModel.anime.image!!.preview!!,
                    nextEpisodeAt = cardModel.nextEpisodeAt!!
                )
            }
        }
    }
}