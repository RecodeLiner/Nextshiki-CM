package com.rcl.nextshiki.base.search.searchedelementscreen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rcl.nextshiki.base.coroutineScope
import com.rcl.nextshiki.base.search.SearchComponent
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.koin
import com.rcl.nextshiki.models.searchobject.ObjById
import com.rcl.nextshiki.models.searchobject.anime.AnimeObject
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SearchedElementComponent(
    id: Int,
    val type: SearchType,
    context: ComponentContext,
    val navigator: StackNavigation<SearchComponent.SearchConfiguration>,
) : ComponentContext by context, ISearchedElement {
    private val _searchedElement = MutableValue(ObjById())
    override val searchedElement: Value<ObjById> = _searchedElement
    override fun popBack() {
        navigator.pop()
    }

    val _selectedAnime = MutableValue(AnimeObject())
    val selectedAnime: Value<AnimeObject> = _selectedAnime

    private val coroutine = coroutineScope(MainScope().coroutineContext + SupervisorJob())

    init {
        lifecycle.doOnCreate {
            coroutine.launch{
                when(type){
                    SearchType.Anime -> {
                        _selectedAnime.value = koin.get<KtorRepository>().getAnimeById(id)
                    }
                    SearchType.Manga -> {}
                    SearchType.Ranobe -> {}
                    SearchType.People -> {}
                    SearchType.Users -> {}
                }
            }
        }
    }
}