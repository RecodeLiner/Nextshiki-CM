package com.rcl.nextshiki.base.search.mainsearchscreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.state.ToggleableState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.rcl.nextshiki.base.coroutineScope
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.koin
import com.rcl.nextshiki.models.getLists.GenreWithState
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class MainSearchComponent(context: ComponentContext) : ComponentContext by context, IMainSearch {
    private val _text = MutableValue("")

    private val mainScope = coroutineScope(MainScope().coroutineContext + SupervisorJob())

    private val _currentType = MutableValue(SearchType.Anime)
    override val typeList: List<SearchType> = SearchType.entries
    override val currentType: Value<SearchType> = _currentType
    override val searchedList = mutableStateListOf<SearchCardModel>()
    override val genresList = mutableStateListOf<GenreWithState>()
    override var text: Value<String> = _text

    init {
        lifecycle.subscribe(
            object : Lifecycle.Callbacks {
                override fun onCreate() {
                    searchObject(text = text.value)
                    mainScope.launch {
                        val list = koin.get<KtorRepository>().getGenres()
                        genresList.addAll(list.map { obj ->
                            GenreWithState(obj, ToggleableState.Off)
                        })
                    }
                }
            }
        )
    }

    override fun onTextChanged(value: String) {
        clearList()
        _text.value = value
    }

    override fun updateType(type: SearchType) {
        _currentType.value = type
    }

    override fun clearList() {
        searchedList.clear()
    }

    override fun searchObject(text: String) {
        clearList()
        mainScope.launch {
            when (currentType.value) {
                SearchType.Anime -> {
                    koin.get<KtorRepository>().searchAnime(search = text).map { item ->
                        item.image?.let {
                            SearchCardModel(
                                id = item.id,
                                image = it,
                                english = item.name,
                                russian = item.russian
                            )
                        }?.let {
                            searchedList.add(
                                it
                            )
                        }
                    }
                }

                SearchType.Manga -> {
                    koin.get<KtorRepository>().searchManga(search = text).map { item ->
                        item.image?.let {
                            SearchCardModel(
                                id = item.id,
                                image = it,
                                english = item.name,
                                russian = item.russian
                            )
                        }?.let {
                            searchedList.add(
                                it
                            )
                        }
                    }
                }

                SearchType.Ranobe -> {
                    koin.get<KtorRepository>().searchRanobe(search = text).map { item ->
                        item.image?.let {
                            SearchCardModel(
                                id = item.id,
                                image = it,
                                english = item.name,
                                russian = item.russian
                            )
                        }?.let {
                            searchedList.add(
                                it
                            )
                        }
                    }
                }
                SearchType.People -> {
                    //Wait when shikimori finally do this
                }

                SearchType.Users -> {
                    koin.get<KtorRepository>().searchUser(search = text).map { item ->
                        item.image?.let {
                            SearchCardModel(
                                id = item.id,
                                image = it,
                                english = item.name,
                                russian = item.russian
                            )
                        }?.let {
                            searchedList.add(
                                it
                            )
                        }
                    }
                }
            }
        }
    }

    override fun navigateToSearchedObject(id: Int, type: SearchType) {
        navigator.bringToFront(
            SearchComponent
                .SearchConfiguration
                .SearchedElementScreen(
                    id = id,
                    type = type
                )
        )
    }
}