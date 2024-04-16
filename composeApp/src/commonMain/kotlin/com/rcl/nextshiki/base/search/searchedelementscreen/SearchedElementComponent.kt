package com.rcl.nextshiki.base.search.searchedelementscreen

import Nextshiki.composeApp.BuildConfig
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.rcl.nextshiki.base.WebResourceConstitute
import com.rcl.nextshiki.base.search.SearchComponent
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.SimpleSearchModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchedElementComponent(
    val id: String,
    val contentType: SearchType,
    context: ComponentContext,
    val navigator: StackNavigation<SearchComponent.SearchConfiguration>,
) : ComponentContext by context, KoinComponent, WebResourceConstitute {
    private var _searchedElement = MutableValue<CommonSearchInterface>(SimpleSearchModel())
    val searchedElement = _searchedElement
    private val ktorRepository: KtorRepository by inject()

    fun popBack() {
        navigator.pop()
    }

    @OptIn(ExperimentalDecomposeApi::class)
    fun navigateTo(contentType: SearchType, id: String) {
        navigator.pushNew(SearchComponent.SearchConfiguration.SearchedElementScreen(id = id, contentType = contentType))
    }

    private val coroutine = CoroutineScope(Dispatchers.IO)

    init {
        lifecycle.doOnCreate {
            coroutine.launch {
                when (contentType) {
                    SearchType.Anime -> {
                        _searchedElement.value = ktorRepository.getAnimeById(id)
                    }

                    SearchType.Manga -> {
                        _searchedElement.value = ktorRepository.getMangaById(id)
                    }

                    SearchType.Ranobe -> {
                        _searchedElement.value = ktorRepository.getRanobeById(id)
                    }

                    SearchType.People -> {
                        _searchedElement.value = ktorRepository.getPeopleById(id)
                    }

                    SearchType.Users -> {
                        _searchedElement.value = ktorRepository.getUserById(id)
                    }

                    SearchType.Characters -> {
                        _searchedElement.value = ktorRepository.getCharacter(id)
                    }
                }
            }
        }
        lifecycle.doOnDestroy {
            _searchedElement.value = SimpleSearchModel()
        }
    }

    override val webUri = MutableValue("${BuildConfig.DOMAIN}/${contentType.path}/$id")
}