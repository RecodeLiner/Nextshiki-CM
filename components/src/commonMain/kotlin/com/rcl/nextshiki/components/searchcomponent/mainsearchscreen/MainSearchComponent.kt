package com.rcl.nextshiki.components.searchcomponent.mainsearchscreen

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rcl.nextshiki.components.RootComponent
import com.rcl.nextshiki.components.RootComponent.TopLevelConfiguration.SearchScreenConfiguration.SearchedElementConfiguration
import com.rcl.nextshiki.di.KtorRepository
import com.rcl.nextshiki.di.language.LanguageRepo
import com.rcl.nextshiki.di.paging.SearchPagingSource
import com.rcl.nextshiki.models.genres.ETriState
import com.rcl.nextshiki.models.genres.GenreWithState
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.SearchListItem
import com.rcl.nextshiki.models.searchobject.SearchType
import com.rcl.nextshiki.models.universal.Image
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainSearchComponent(
    context: ComponentContext,
    private val navigator: StackNavigation<RootComponent.TopLevelConfiguration>,
) : ComponentContext by context, KoinComponent {
    val vm = instanceKeeper.getOrCreate { MainSearchViewModel() }

    class MainSearchViewModel : InstanceKeeper.Instance, KoinComponent {
        private val ktorRepository: KtorRepository by inject()
        val languageRepo: LanguageRepo by inject()
        val viewModelScope = CoroutineScope(Dispatchers.IO)

        val typeList: List<SearchType> = SearchType.entries
        val currentType = MutableStateFlow(SearchType.Anime)
        val text = MutableValue("")
        val genresList = MutableValue<List<GenreWithState>>(emptyList())

        // Paging data flow
        @OptIn(ExperimentalCoroutinesApi::class)
        val pagingDataFlow = currentType.flatMapLatest { type ->
            Pager(PagingConfig(pageSize = 50)) {
                SearchPagingSource(
                    repository = ktorRepository,
                    searchType = type,
                    search = text.value
                )
            }.flow.cachedIn(viewModelScope)
        }

        fun onCreate() {
            viewModelScope.launch {
                val list = ktorRepository.getGenres()
                genresList.update {
                    it + list.map { GenreWithState(it, ETriState.OFF) }
                }
            }
        }

        fun onSearchBarValueChange(str: String) {
            onTextChanged(str)
        }

        private fun onTextChanged(value: String) {
            text.update { value }
            refreshPagingData()
        }

        fun updateType(type: SearchType) {
            currentType.update { type }
            refreshPagingData()
        }

        private fun refreshPagingData() {
            pagingDataFlow
        }
    }

    init {
        lifecycle.doOnCreate {
            vm.onCreate()
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

fun SearchListItem.toSearchCard(type: SearchType) : SearchCardModel {
    return SearchCardModel (
        image = this.image ?: Image(),
        english = this.name,
        russian = this.russian,
        searchType = type,
        id = this.id ?: 0
    )
}
