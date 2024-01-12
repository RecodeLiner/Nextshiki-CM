package com.rcl.nextshiki.base.search.searchedelementscreen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.rcl.nextshiki.base.coroutineScope
import com.rcl.nextshiki.base.search.SearchComponent
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.koin
import com.rcl.nextshiki.models.searchobject.anime.AnimeObject
import com.rcl.nextshiki.models.searchobject.manga.MangaObject
import com.rcl.nextshiki.models.searchobject.people.PeopleObject
import com.rcl.nextshiki.models.searchobject.ranobe.RanobeObject
import com.rcl.nextshiki.models.searchobject.users.UserObject
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SearchedElementComponent(
    id: Int,
    val type: SearchType,
    context: ComponentContext,
    val navigator: StackNavigation<SearchComponent.SearchConfiguration>,
) : ComponentContext by context, ISearchedElement {
    private var _searchedAnimeElement = MutableValue(AnimeObject())
    private var _searchedMangaElement = MutableValue(MangaObject())
    private var _searchedRanobeElement = MutableValue(RanobeObject())
    private var _searchedPeopleElement = MutableValue(PeopleObject())
    private var _searchedUserElement = MutableValue(UserObject())
    override val searchedAnimeElement = _searchedAnimeElement
    override val searchedMangaElement = _searchedMangaElement
    override val searchedRanobeElement = _searchedRanobeElement
    override val searchedPeopleElement = _searchedPeopleElement
    override val searchedUserElement = _searchedUserElement

    override fun popBack() {
        navigator.pop()
    }

    private val coroutine = coroutineScope(MainScope().coroutineContext + SupervisorJob())

    init {
        lifecycle.doOnCreate {
            coroutine.launch{
                when(type){
                    SearchType.Anime -> {
                        _searchedAnimeElement.value = koin.get<KtorRepository>().getAnimeById(id)
                    }
                    SearchType.Manga -> {
                        _searchedMangaElement.value = koin.get<KtorRepository>().getMangaById(id)
                    }
                    SearchType.Ranobe -> {
                        _searchedRanobeElement.value = koin.get<KtorRepository>().getRanobeById(id)
                    }
                    SearchType.People -> {
                        _searchedPeopleElement.value = koin.get<KtorRepository>().getPeopleById(id)
                    }
                    SearchType.Users -> {
                        _searchedUserElement.value = koin.get<KtorRepository>().getUserById(id)
                    }
                }
            }
        }
        lifecycle.doOnDestroy {
            when(type){
                SearchType.Anime -> {
                    _searchedAnimeElement.value = AnimeObject()
                }
                SearchType.Manga -> {
                    _searchedMangaElement.value = MangaObject()
                }
                SearchType.Ranobe -> {
                    _searchedRanobeElement.value = RanobeObject()
                }
                SearchType.People -> {
                    _searchedPeopleElement.value = PeopleObject()
                }
                SearchType.Users -> {
                    _searchedUserElement.value = UserObject()
                }
            }
        }
    }
}