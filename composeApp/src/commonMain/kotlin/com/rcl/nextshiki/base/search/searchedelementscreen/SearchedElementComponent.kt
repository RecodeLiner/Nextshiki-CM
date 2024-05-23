package com.rcl.nextshiki.base.search.searchedelementscreen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.base.RootComponent.TopLevelConfiguration.SearchScreenConfiguration.SearchedElementScreen
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.locale.CustomLocale.getCurrentLocale
import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.SimpleSearchModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchedElementComponent(
    val id: String,
    val contentType: SearchType,
    context: ComponentContext,
    val navigator: StackNavigation<RootComponent.TopLevelConfiguration>,
) : ComponentContext by context, KoinComponent {
    val searchedElement = MutableValue<CommonSearchInterface>(SimpleSearchModel())
    private val ktorRepository: KtorRepository by inject()
    private val coroutine = CoroutineScope(Dispatchers.IO)

    fun popBack() {
        navigator.pop()
    }

    fun navigateTo(contentType: SearchType, id: String) {
        navigator.bringToFront(
            SearchedElementScreen(
                id = id,
                contentType = contentType
            )
        )
    }

    init {
        lifecycle.doOnCreate {
            coroutine.launch {
                val content: CommonSearchInterface
                when (contentType) {
                    SearchType.Anime -> {
                        val rolesList =
                            ktorRepository.getRolesById(id, contentType = SearchType.Anime.apiPath)
                        content = ktorRepository.getAnimeById(id, locale = getCurrentLocale())
                            .copy(rolesList = rolesList)
                    }

                    SearchType.Manga -> {
                        val rolesList =
                            ktorRepository.getRolesById(id, contentType = SearchType.Manga.apiPath)
                        content = ktorRepository.getMangaById(id, locale = getCurrentLocale())
                            .copy(rolesList = rolesList)
                    }

                    SearchType.Ranobe -> {
                        val rolesList =
                            ktorRepository.getRolesById(id, contentType = SearchType.Ranobe.apiPath)
                        content = ktorRepository.getRanobeById(id, locale = getCurrentLocale())
                            .copy(rolesList = rolesList)
                    }

                    SearchType.People -> {
                        content = ktorRepository.getPeopleById(id, locale = getCurrentLocale())
                    }

                    SearchType.Users -> {
                        content = ktorRepository.getUserById(id, locale = getCurrentLocale())
                    }

                    SearchType.Characters -> {
                        content = ktorRepository.getCharacter(id, locale = getCurrentLocale())
                    }
                }
                searchedElement.update { content }
            }
        }
        lifecycle.doOnDestroy {
            searchedElement.update { SimpleSearchModel() }
        }
    }

    //val webUri = MutableValue("${BuildConfig.DOMAIN}/${contentType.path}/$id")
}