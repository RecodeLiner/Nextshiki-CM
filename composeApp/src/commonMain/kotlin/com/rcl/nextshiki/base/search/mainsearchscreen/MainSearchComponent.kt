package com.rcl.nextshiki.base.search.mainsearchscreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.state.ToggleableState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.rcl.nextshiki.base.WebResourceConstitute
import com.rcl.nextshiki.base.search.SearchComponent
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.genres.GenreWithState
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.SearchListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class MainSearchComponent(
    context: ComponentContext,
    private val navigator: StackNavigation<SearchComponent.SearchConfiguration>,
) : ComponentContext by context, KoinComponent, WebResourceConstitute {
    private val _text = MutableValue("")
    private val ktorRepository: KtorRepository by inject()

    private val scope = CoroutineScope(Dispatchers.Default)
    private val _currentType = MutableValue(SearchType.Anime)
    private val _currentPage = MutableValue(1)
    private val _possibleToAdd = MutableValue(true)
    val typeList: List<SearchType> = SearchType.entries
    private val currentPage = _currentPage
    private val possibleToAdd = _possibleToAdd
    val currentType = _currentType
    val searchedList = mutableStateListOf<SearchCardModel>()
    val genresList = mutableStateListOf<GenreWithState>()
    val text = _text

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

    fun onTextChanged(value: String) {
        clearList()
        _text.value = value
    }

    fun updateType(type: SearchType) {
        _currentType.value = type
        _currentPage.value = 1
    }

    fun clearList() {
        searchedList.clear()
        clearPage()
    }

    fun searchObject(text: String) {
        if (!possibleToAdd.value) return
        scope.launch {
            val searchResult: List<SearchListItem> = when (currentType.value) {
                SearchType.Anime -> ktorRepository.searchAnime(search = text, page = currentPage.value)
                SearchType.Manga -> ktorRepository.searchManga(search = text, page = currentPage.value)
                SearchType.Ranobe -> ktorRepository.searchRanobe(search = text, page = currentPage.value)
                SearchType.People -> ktorRepository.searchPeople(search = text)
                SearchType.Users -> ktorRepository.searchUser(search = text, page = currentPage.value)
                SearchType.Characters -> ktorRepository.searchCharacters(search = text)
            }
            searchResult.map { item ->
                item.image?.let { image ->
                    SearchCardModel(
                        id = item.id,
                        image = image,
                        english = item.nickname?: item.name,
                        russian = item.nickname?: item.russian
                    )
                }?.let { cardModel ->
                    if (searchedList.any { it.id == cardModel.id }) {
                        possibleToAdd.value = false
                        return@map
                    }
                    searchedList.add(cardModel)
                }
            }
        }
    }

    fun updatePageList() {
        if (currentType.value != SearchType.People) {
            incPage()
            searchObject(_text.value)
        }
    }

    fun setImpossibleToAdd() {
        _possibleToAdd.value = false
    }

    fun resetImpossibleToAdd() {
        _possibleToAdd.value = true
    }

    fun incPage() {
        _currentPage.value++
    }

    fun clearPage() {
        _currentPage.value = 1
    }

    fun navigateToSearchedObject(id: String, contentType: SearchType) {
        navigator.bringToFront(
            SearchComponent
                .SearchConfiguration
                .SearchedElementScreen(
                    id = id,
                    contentType = contentType
                )
        )
    }

    override val webUri = null
}