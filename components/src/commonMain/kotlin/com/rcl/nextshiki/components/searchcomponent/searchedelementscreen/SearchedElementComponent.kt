package com.rcl.nextshiki.components.searchcomponent.searchedelementscreen

import Nextshiki.resources.BuildConfig
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rcl.nextshiki.components.IWebUri
import com.rcl.nextshiki.components.RootComponent
import com.rcl.nextshiki.components.RootComponent.TopLevelConfiguration.SearchScreenConfiguration.SearchedElementConfiguration
import com.rcl.nextshiki.di.KtorRepository
import com.rcl.nextshiki.di.language.LanguageRepo
import com.rcl.nextshiki.models.searchobject.CommonSearchInterface
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.SearchType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchedElementComponent(
    cardModel: SearchCardModel,
    contentType: SearchType,
    context: ComponentContext,
    private val navigator: StackNavigation<RootComponent.TopLevelConfiguration>,
) : ComponentContext by context {

    val vm = instanceKeeper.getOrCreate { SearchedElementViewModel(cardModel, contentType) }

    fun popBack() {
        navigator.pop()
    }

    fun navigateTo(model: SearchCardModel, contentType: SearchType) {
        navigator.bringToFront(
            SearchedElementConfiguration(
                cardModel = model,
                contentType = contentType
            )
        )
    }

    class SearchedElementViewModel(
        private val cardModel: SearchCardModel,
        val contentType: SearchType
    ) : InstanceKeeper.Instance, KoinComponent, IWebUri {
        private val networkScope = CoroutineScope(Dispatchers.IO)
        private val ktorRepository: KtorRepository by inject()
        val languageRepo: LanguageRepo by inject()
        val searchedElement = MutableValue<CommonSearchInterface>(cardModel)

        fun onCreate() {
            networkScope.launch {
                val locale = languageRepo.getCurrentCode()
                val content: CommonSearchInterface
                when (contentType) {
                    SearchType.Anime -> {
                        val rolesList =
                            ktorRepository.getRolesById(
                                cardModel.id.toString(),
                                contentType = SearchType.Anime.apiPath
                            )
                        val franchiseModel =
                            ktorRepository.getFranchise(cardModel.id.toString(), SearchType.Anime)
                        content = ktorRepository.getAnimeById(
                            cardModel.id.toString(),
                            locale = locale
                        )
                            .copy(
                                rolesList = rolesList,
                                franchiseModel = franchiseModel
                            ) as CommonSearchInterface
                    }

                    SearchType.Manga -> {
                        val rolesList =
                            ktorRepository.getRolesById(
                                cardModel.id.toString(),
                                contentType = SearchType.Manga.apiPath
                            )
                        val franchiseModel =
                            ktorRepository.getFranchise(cardModel.id.toString(), SearchType.Manga)
                        content = ktorRepository.getMangaById(
                            cardModel.id.toString(),
                            locale = locale
                        )
                            .copy(rolesList = rolesList, franchiseModel = franchiseModel)
                    }

                    SearchType.Ranobe -> {
                        val rolesList =
                            ktorRepository.getRolesById(
                                cardModel.id.toString(),
                                contentType = SearchType.Ranobe.apiPath
                            )
                        val franchiseModel =
                            ktorRepository.getFranchise(cardModel.id.toString(), SearchType.Ranobe)
                        content = ktorRepository.getRanobeById(
                            cardModel.id.toString(),
                            locale = locale
                        )
                            .copy(rolesList = rolesList, franchiseModel = franchiseModel)
                    }

                    SearchType.People -> {
                        content = ktorRepository.getPeopleById(
                            cardModel.id.toString(),
                            locale = locale
                        )
                    }

                    SearchType.Users -> {
                        content = ktorRepository.getUserById(
                            cardModel.id.toString(),
                            locale = locale
                        )
                    }

                    SearchType.Characters -> {
                        content = ktorRepository.getCharacter(
                            cardModel.id.toString(),
                            locale = locale
                        )
                    }
                }
                searchedElement.update { content }
            }
        }
        override val currentLink =
            MutableValue("${BuildConfig.DOMAIN}/${contentType.apiPath}/${cardModel.id}")
    }

    init {
        lifecycle.doOnCreate {
            vm.onCreate()
        }
    }
}
