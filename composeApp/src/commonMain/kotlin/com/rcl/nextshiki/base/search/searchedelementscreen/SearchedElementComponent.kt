package com.rcl.nextshiki.base.search.searchedelementscreen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.rcl.nextshiki.base.search.SearchComponent
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.SimpleSearchModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default

class SearchedElementComponent(
    private val ktorRepository: KtorRepository,
    id: Int,
    val type: SearchType,
    context: ComponentContext,
    val navigator: StackNavigation<SearchComponent.SearchConfiguration>,
) : ComponentContext by context, ISearchedElement {
    private var _searchedElement = MutableValue<CommonSearchInterface>(SimpleSearchModel())
    override val searchedElement = _searchedElement

    override fun popBack() {
        navigator.pop()
    }

    private val coroutine = CoroutineScope(Default)

    init {
        lifecycle.doOnCreate {
            coroutine.launch{
                when(type){
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
                }
            }
        }
        lifecycle.doOnDestroy {
            _searchedElement.value = SimpleSearchModel()
        }
    }
}