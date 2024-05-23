package com.rcl.nextshiki.base.search.mainsearchscreen

import Nextshiki.composeApp.BuildConfig
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.state.ToggleableState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.base.RootComponent.TopLevelConfiguration.SearchScreenConfiguration.SearchedElementScreen
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.models.genres.GenreWithState
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.SearchListItem
import com.rcl.nextshiki.models.universal.Image
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class MainSearchComponent(
    context: ComponentContext,
    private val navigator: StackNavigation<RootComponent.TopLevelConfiguration>,
) : ComponentContext by context, KoinComponent {
    private val ktorRepository: KtorRepository by inject()

    private val scope = CoroutineScope(Dispatchers.Main)
    private val currentPage = MutableValue(1)
    val typeList: ImmutableList<SearchType> = SearchType.entries.toPersistentList()
    val currentType = MutableValue(SearchType.Anime)
    val isEndOfListReached = MutableValue(false)
    val searchedList = mutableStateListOf<SearchCardModel>()
    val genresList = mutableStateListOf<GenreWithState>()
    val text = MutableValue("")

    init {
        lifecycle
            .doOnCreate {
                scope.launch {
                    searchObject(text = text.value)
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
        text.update { value }
    }

    fun updateType(type: SearchType) {
        currentType.update { type }
        currentPage.update { 1 }
    }

    fun clearList() {
        searchedList.clear()
        currentPage.update { 1 }
    }

    fun searchObject(text: String) {
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
                    item.id?.let {
                        SearchCardModel(
                            id = it,
                            image = image,
                            english = item.nickname ?: item.name,
                            russian = item.nickname ?: item.russian
                        )
                    }
                }?.let { cardModel ->
                    if (searchedList.any { it.id == cardModel.id }) {
                        return@map
                    }
                    searchedList.add(cardModel)
                }
            }
            isEndOfListReached.update { false }
            delay(5_000)
        }
    }

    fun updatePageList() {
        if (currentType.value != SearchType.People) {
            currentPage.update { it + 1 }
            searchObject(text.value)
        }
    }

    fun navigateToSearchedObject(id: String, contentType: SearchType) {
        navigator.bringToFront(
            SearchedElementScreen(
                id = id,
                contentType = contentType
            )
        )
    }
}

fun getValidImageUrl(image: Image): String? {
    return when {
        image.original != null -> {
            getValidUrlByLink(image.original)
        }

        image.x160 != null -> {
            getValidUrlByLink(image.x160)
        }

        else -> {
            null
        }
    }
}

fun getValidUrlByLink(string: String): String {
    return if (string.contains("https://") || string.contains("http://")) {
        string
    } else {
        BuildConfig.DOMAIN + string
    }
}