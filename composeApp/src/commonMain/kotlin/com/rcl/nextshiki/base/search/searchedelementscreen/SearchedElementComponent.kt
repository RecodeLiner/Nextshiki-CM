package com.rcl.nextshiki.base.search.searchedelementscreen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.rcl.nextshiki.Koin.koin
import com.rcl.nextshiki.base.coroutineScope
import com.rcl.nextshiki.base.search.SearchComponent
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.SimpleSearchModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SearchedElementComponent(
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

    private val coroutine = coroutineScope(MainScope().coroutineContext + SupervisorJob())

    init {
        lifecycle.doOnCreate {
            coroutine.launch{
                when(type){
                    SearchType.Anime -> {
                        _searchedElement.value = koin.get<KtorRepository>().getAnimeById(id)
                    }
                    SearchType.Manga -> {
                        _searchedElement.value = koin.get<KtorRepository>().getMangaById(id)
                    }
                    SearchType.Ranobe -> {
                        _searchedElement.value = koin.get<KtorRepository>().getRanobeById(id)
                    }
                    SearchType.People -> {
                        _searchedElement.value = koin.get<KtorRepository>().getPeopleById(id)
                    }
                    SearchType.Users -> {
                        _searchedElement.value = koin.get<KtorRepository>().getUserById(id)
                    }
                }
            }
        }
        lifecycle.doOnDestroy {
            _searchedElement.value = SimpleSearchModel()
        }
    }
}