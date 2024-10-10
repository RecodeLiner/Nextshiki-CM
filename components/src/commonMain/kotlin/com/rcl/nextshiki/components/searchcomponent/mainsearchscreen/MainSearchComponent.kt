package com.rcl.nextshiki.components.searchcomponent.mainsearchscreen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.rcl.nextshiki.components.RootComponent
import com.rcl.nextshiki.components.RootComponent.TopLevelConfiguration.SearchScreenConfiguration.SearchedElementConfiguration
import com.rcl.nextshiki.di.KtorRepository
import com.rcl.nextshiki.di.language.LanguageRepo
import com.rcl.nextshiki.models.genres.ETriState
import com.rcl.nextshiki.models.genres.GenreWithState
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.SearchListItem
import com.rcl.nextshiki.models.searchobject.SearchType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.text.endsWith

class MainSearchComponent(
    context: ComponentContext,
    private val navigator: StackNavigation<RootComponent.TopLevelConfiguration>,
) : ComponentContext by context, KoinComponent {
    val vm = instanceKeeper.getOrCreate { MainSearchViewModel() }

    class MainSearchViewModel: InstanceKeeper.Instance, KoinComponent {
        private val ktorRepository: KtorRepository by inject()
        val languageRepo: LanguageRepo by inject()

        private val scope = CoroutineScope(Dispatchers.Main)
        private val currentPage = MutableValue(1)
        val typeList: List<SearchType> = SearchType.entries
        val currentType = MutableValue(SearchType.Anime)
        val isEndOfListReached = MutableValue(false)
        val searchedList = MutableValue<List<SearchCardModel>>(emptyList())
        val genresList = MutableValue<List<GenreWithState>>(emptyList())
        val text = MutableValue("")

        fun onCreate() {
            scope.launch {
                searchObject(text = text.value)
                val list = ktorRepository.getGenres()
                genresList.update {
                    it + list.map {
                        GenreWithState(it, ETriState.OFF)
                    }
                }
            }
        }

        fun onSearchBarValueChange(str: String) {
            onTextChanged(str)
            if (text.value.endsWith("\n")) {
                onTextChanged(str.dropLast(1))
                clearList()
                searchObject(text.value)
            }
        }

        fun updatePageList() {
            if (currentType.value != SearchType.People) {
                currentPage.update { it + 1 }
                searchObject(text.value)
            }
        }

        fun onTextChanged(value: String) {
            clearList()
            text.update { value }
        }

        fun updateType(type: SearchType) {
            currentType.update { type }
            currentPage.update { 1 }
        }

        fun clearList() {
            searchedList.update { emptyList() }
            currentPage.update { 1 }
        }

        fun typeRowClick(type: SearchType, text: String) {
            updateType(type)
            clearList()
            searchObject(text)
        }

        fun searchObject(text: String) {
            scope.launch {
                val searchResult: List<SearchListItem> = when (currentType.value) {
                    SearchType.Anime -> ktorRepository.searchAnime(
                        search = text,
                        page = currentPage.value
                    )

                    SearchType.Manga -> ktorRepository.searchManga(
                        search = text,
                        page = currentPage.value
                    )

                    SearchType.Ranobe -> ktorRepository.searchRanobe(
                        search = text,
                        page = currentPage.value
                    )

                    SearchType.People -> ktorRepository.searchPeople(search = text)
                    SearchType.Users -> ktorRepository.searchUser(
                        search = text,
                        page = currentPage.value
                    )

                    SearchType.Characters -> ktorRepository.searchCharacters(search = text)
                }
                searchResult.map { item ->
                    item.image?.let { image ->
                        item.id?.let {
                            SearchCardModel(
                                id = it,
                                image = image,
                                english = item.nickname ?: item.name,
                                russian = item.nickname ?: item.russian,
                                searchType = currentType.value
                            )
                        }
                    }?.let { cardModel ->
                        if (searchedList.value.any { it.id == cardModel.id }) {
                            return@map
                        }
                        searchedList.update {
                            it + cardModel
                        }
                    }
                }
                isEndOfListReached.update { false }
                delay(5_000)
            }
        }
    }

    init {
        lifecycle
            .doOnCreate {
                vm.onCreate()
            }
        lifecycle
            .doOnDestroy {

            }
    }

    fun navigateToSearchedObject(searchModel: SearchCardModel, contentType: SearchType) {
        navigator.pushToFront(
            SearchedElementConfiguration(
                cardModel = searchModel,
                contentType = contentType
            )
        )
    }
}
