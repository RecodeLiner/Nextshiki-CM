package com.rcl.nextshiki.base.search.searchedelementscreen

import androidx.compose.runtime.Stable
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
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType.Anime
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType.Manga
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType.Ranobe
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.locale.CustomLocale.getCurrentLocale
import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.SimpleSearchModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Stable
class SearchedElementComponent(
    val cardModel: SearchCardModel,
    val contentType: SearchType,
    context: ComponentContext,
    val navigator: StackNavigation<RootComponent.TopLevelConfiguration>,
) : ComponentContext by context, KoinComponent {
    val searchedElement = MutableValue<CommonSearchInterface>(cardModel)
    private val ktorRepository: KtorRepository by inject()
    private val coroutine = CoroutineScope(Dispatchers.IO)

    fun popBack() {
        navigator.pop()
    }

    fun navigateTo(model: SearchCardModel, contentType: SearchType) {
        navigator.bringToFront(
            SearchedElementScreen(
                cardModel = model,
                contentType = contentType
            )
        )
    }

    init {
        lifecycle.doOnCreate {
            coroutine.launch {
                val content: CommonSearchInterface
                when (contentType) {
                    Anime -> {
                        val rolesList =
                            ktorRepository.getRolesById(cardModel.id.toString(), contentType = Anime.apiPath)
                        val franchiseModel =
                            ktorRepository.getFranchise(cardModel.id.toString(), Anime)
                        content = ktorRepository.getAnimeById(cardModel.id.toString(), locale = getCurrentLocale())
                            .copy(rolesList = rolesList, franchiseModel = franchiseModel)
                    }

                    Manga -> {
                        val rolesList =
                            ktorRepository.getRolesById(cardModel.id.toString(), contentType = Manga.apiPath)
                        val franchiseModel =
                            ktorRepository.getFranchise(cardModel.id.toString(), Manga)
                        content = ktorRepository.getMangaById(cardModel.id.toString(), locale = getCurrentLocale())
                            .copy(rolesList = rolesList, franchiseModel = franchiseModel)
                    }

                    Ranobe -> {
                        val rolesList =
                            ktorRepository.getRolesById(cardModel.id.toString(), contentType = Ranobe.apiPath)
                        val franchiseModel =
                            ktorRepository.getFranchise(cardModel.id.toString(), Ranobe)
                        content = ktorRepository.getRanobeById(cardModel.id.toString(), locale = getCurrentLocale())
                            .copy(rolesList = rolesList, franchiseModel = franchiseModel)
                    }

                    SearchType.People -> {
                        content = ktorRepository.getPeopleById(cardModel.id.toString(), locale = getCurrentLocale())
                    }

                    SearchType.Users -> {
                        content = ktorRepository.getUserById(cardModel.id.toString(), locale = getCurrentLocale())
                    }

                    SearchType.Characters -> {
                        content = ktorRepository.getCharacter(cardModel.id.toString(), locale = getCurrentLocale())
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
