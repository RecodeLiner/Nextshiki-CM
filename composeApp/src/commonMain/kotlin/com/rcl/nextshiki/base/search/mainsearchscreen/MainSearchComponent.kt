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
) : ComponentContext by context, IMainSearch, KoinComponent, WebResourceConstitute {
    private val _text = MutableValue("")
    private val ktorRepository: KtorRepository by inject()

    private val scope = CoroutineScope(Dispatchers.Default)
    private val _currentType = MutableValue(SearchType.Anime)
    private val _currentPage = MutableValue(1)
    private val _possibleToAdd = MutableValue(true)
    override val typeList: List<SearchType> = SearchType.entries
    override val currentPage = _currentPage
    override val possibleToAdd = _possibleToAdd
    override val currentType = _currentType
    override val searchedList = mutableStateListOf<SearchCardModel>()
    override val genresList = mutableStateListOf<GenreWithState>()
    override val text = _text

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
        _currentPage.value = 1
    }

    override fun clearList() {
        searchedList.clear()
        clearPage()
    }

    override fun searchObject(text: String) {
        if (!possibleToAdd.value) return
        scope.launch {
            val searchResult: List<SearchListItem> = when (currentType.value) {
                SearchType.Anime -> ktorRepository.searchAnime(search = text, page = currentPage.value)
                SearchType.Manga -> ktorRepository.searchManga(search = text, page = currentPage.value)
                SearchType.Ranobe -> ktorRepository.searchRanobe(search = text, page = currentPage.value)
                SearchType.People -> ktorRepository.searchPeople(search = text)
                SearchType.Users -> ktorRepository.searchUser(search = text, page = currentPage.value)
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

    override fun updatePageList() {
        if (currentType.value != SearchType.People) {
            incPage()
            searchObject(_text.value)
        }
    }

    override fun setImpossibleToAdd() {
        _possibleToAdd.value = false
    }

    override fun resetImpossibleToAdd() {
        _possibleToAdd.value = true
    }

    override fun incPage() {
        _currentPage.value++
    }

    override fun clearPage() {
        _currentPage.value = 1
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