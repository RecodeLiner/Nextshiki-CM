package com.rcl.nextshiki.base.search.mainsearchscreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.state.ToggleableState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.rcl.nextshiki.base.WebResourceConstitute
import com.rcl.nextshiki.base.search.SearchComponent
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.genres.GenreWithState
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class MainSearchComponent(
    context: ComponentContext,
    private val navigator: StackNavigation<SearchComponent.SearchConfiguration>,
) : ComponentContext by context, IMainSearch, KoinComponent, WebResourceConstitute {
    private val _text = MutableValue("")
    private val ktorRepository: KtorRepository by inject()

    private val scope = CoroutineScope(Dispatchers.Default)

    private val _currentType = MutableValue(SearchType.Anime)
    override val typeList: List<SearchType> = SearchType.entries
    override val currentType: Value<SearchType> = _currentType
    override val searchedList = mutableStateListOf<SearchCardModel>()
    override val genresList = mutableStateListOf<GenreWithState>()
    override var text: Value<String> = _text

    init {
        lifecycle
            .doOnCreate {
                searchObject(text = text.value)
                scope.launch {
                    val list = ktorRepository.getGenres()
                    genresList.addAll(list.map { obj ->
                        GenreWithState(obj, ToggleableState.Off)
                    })
                }
            }
        lifecycle
            .doOnDestroy {

            }
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
        scope.launch {
            when (currentType.value) {
                SearchType.Anime -> {
                    ktorRepository.searchAnime(search = text).map { item ->
                        item.image?.let { image ->
                            SearchCardModel(
                                id = item.id,
                                image = image,
                                english = item.name,
                                russian = item.russian
                            )
                        }?.let { cardModel ->
                            searchedList.add(
                                cardModel
                            )
                        }
                    }
                }

                SearchType.Manga -> {
                    ktorRepository.searchManga(search = text).map { item ->
                        item.image?.let { image ->
                            SearchCardModel(
                                id = item.id,
                                image = image,
                                english = item.name,
                                russian = item.russian
                            )
                        }?.let { cardModel ->
                            searchedList.add(
                                cardModel
                            )
                        }
                    }
                }

                SearchType.Ranobe -> {
                    ktorRepository.searchRanobe(search = text).map { item ->
                        item.image?.let { image ->
                            SearchCardModel(
                                id = item.id,
                                image = image,
                                english = item.name,
                                russian = item.russian
                            )
                        }?.let { cardModel ->
                            searchedList.add(
                                cardModel
                            )
                        }
                    }
                }

                SearchType.People -> {
                    ktorRepository.searchPeople(search = text).map { item ->
                        item.image?.let {
                            SearchCardModel(
                                id = item.id,
                                image = it,
                                english = item.name,
                                russian = item.russian
                            )
                        }?.let { cardModel ->
                            searchedList.add(
                                cardModel
                            )
                        }
                    }
                }

                SearchType.Users -> {
                    ktorRepository.searchUser(search = text).map { item ->
                        item.image?.let { image ->
                            SearchCardModel(
                                id = item.id,
                                image = image,
                                english = item.name,
                                russian = item.russian
                            )
                        }?.let { cardModel ->
                            searchedList.add(
                                cardModel
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

    override val webUri = null
}