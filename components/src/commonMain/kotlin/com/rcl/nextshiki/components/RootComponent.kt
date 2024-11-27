package com.rcl.nextshiki.components

import Nextshiki.resources.BuildConfig.CLIENT_ID
import Nextshiki.resources.BuildConfig.CLIENT_ID_DESK
import Nextshiki.resources.BuildConfig.CLIENT_SECRET
import Nextshiki.resources.BuildConfig.CLIENT_SECRET_DESK
import Nextshiki.resources.BuildConfig.REDIRECT_URI
import Nextshiki.resources.BuildConfig.REDIRECT_URI_DESK
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.doOnStart
import com.rcl.nextshiki.components.newscomponent.mainpage.MainNewsComponent
import com.rcl.nextshiki.components.newscomponent.newspage.NewsPageComponent
import com.rcl.nextshiki.components.profilecomponent.historypage.ProfileHistoryComponent
import com.rcl.nextshiki.components.profilecomponent.mainprofile.MainProfileComponent
import com.rcl.nextshiki.components.profilecomponent.settings.SettingsComponent
import com.rcl.nextshiki.components.searchcomponent.mainsearchscreen.MainSearchComponent
import com.rcl.nextshiki.components.searchcomponent.searchedelementscreen.SearchedElementComponent
import com.rcl.nextshiki.di.KtorRepository
import com.rcl.nextshiki.di.RepositoryModule
import com.rcl.nextshiki.di.language.LanguageModule
import com.rcl.nextshiki.di.language.LanguageRepo
import com.rcl.nextshiki.di.settings.ISettingsRepo
import com.rcl.nextshiki.di.settings.SettingsModuleObject
import com.rcl.nextshiki.models.searchobject.SearchCardModel
import com.rcl.nextshiki.models.searchobject.SearchType
import com.rcl.nextshiki.models.topics.HotTopics
import com.rcl.nextshiki.utils.Platform
import com.rcl.nextshiki.utils.getCurrentPlatform
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class RootComponent(context: ComponentContext) : ComponentContext by context, IWebUri {
    val vm = instanceKeeper.getOrCreate { RootViewModel() }
    private val navigator = StackNavigation<TopLevelConfiguration>()

    val childStack = childStack(
        source = navigator,
        serializer = TopLevelConfiguration.serializer(),
        initialConfiguration = TopLevelConfiguration.MainScreenConfiguration.MainNewsConfiguration,
        handleBackButton = true,
        childFactory = ::createChild
    )

    init {
        lifecycle.doOnStart {
            vm.onCreate()
        }
    }

    class RootViewModel : InstanceKeeper.Instance {
        val viewModelScope = CoroutineScope(Dispatchers.IO)
        private val ktorRepository: KtorRepository = RepositoryModule.getKtorRepository()
        private val settings: ISettingsRepo = SettingsModuleObject.settingsImpl
        val languageRepo: LanguageRepo = LanguageModule.langRepo

        fun deepLinkHandler(link: String) {
            Napier.i("Link is $link")
        }

        fun updateToken() {
            viewModelScope.launch {
                val platform = getCurrentPlatform()
                val code = settings.getValue("refCode") ?: return@launch

                val token = ktorRepository.getToken(
                    code = code,
                    clientSecret = when(platform) {
                        Platform.Mobile -> CLIENT_SECRET
                        Platform.Desktop -> CLIENT_SECRET_DESK
                    },
                    clientID = when(platform) {
                        Platform.Mobile -> CLIENT_ID
                        Platform.Desktop -> CLIENT_ID_DESK
                    },
                    redirectUri = when(platform) {
                        Platform.Mobile -> REDIRECT_URI
                        Platform.Desktop -> REDIRECT_URI_DESK
                    },
                )

                settings.addValue(key = "refCode", value = token.refreshToken.toString())
                if (token.accessToken != null) {
                    RepositoryModule.token = token.accessToken
                }
                if (token.scope != null) {
                    RepositoryModule.scope = token.scope
                }
            }
        }

        fun onCreate() {
            updateToken()
        }
    }

    fun onBack() {
        navigator.pop()
    }

    fun navigateTo(config: TopLevelConfiguration) {
        navigator.pushToFront(config)
    }

    private fun createChild(
        config: TopLevelConfiguration,
        context: ComponentContext,
    ): TopLevelChild {
        return when (config) {
            is TopLevelConfiguration.MainScreenConfiguration.MainNewsConfiguration ->
                TopLevelChild.MainScreenChild.MainNewsChild(
                    MainNewsComponent(context = context, navigator = navigator)
                )

            is TopLevelConfiguration.MainScreenConfiguration.NewsPageConfiguration ->
                TopLevelChild.MainScreenChild.NewsPageChild(
                    NewsPageComponent(
                        context = context,
                        navigator = navigator,
                        topic = config.topic
                    )
                )

            is TopLevelConfiguration.SearchScreenConfiguration.MainSearchConfiguration ->
                TopLevelChild.SearchScreenChild.MainSearchScreenChild(
                    MainSearchComponent(context = context, navigator = navigator)
                )

            is TopLevelConfiguration.SearchScreenConfiguration.SearchedElementConfiguration ->
                TopLevelChild.SearchScreenChild.SearchedElementChild(
                    SearchedElementComponent(
                        context = context,
                        contentType = config.contentType,
                        cardModel = config.cardModel,
                        navigator = navigator
                    )
                )

            is TopLevelConfiguration.ProfileScreenConfiguration.MainProfileConfiguration ->
                TopLevelChild.ProfileScreenChild.MainProfileScreenChild(
                    MainProfileComponent(context = context, navigator = navigator)
                )

            is TopLevelConfiguration.ProfileScreenConfiguration.ProfileHistoryConfiguration ->
                TopLevelChild.ProfileScreenChild.ProfileHistoryScreenChild(
                    ProfileHistoryComponent(context = context, navigator = navigator)
                )

            is TopLevelConfiguration.ProfileScreenConfiguration.SettingsProfileConfiguration ->
                TopLevelChild.ProfileScreenChild.SettingsScreenChild(
                    SettingsComponent(context = context, navigator = navigator)
                )

        }
    }

    sealed class TopLevelChild {
        data object MainScreenChild : TopLevelChild() {
            data class MainNewsChild(val component: MainNewsComponent) : TopLevelChild()
            data class NewsPageChild(val component: NewsPageComponent) : TopLevelChild()
        }

        data object SearchScreenChild : TopLevelChild() {
            data class MainSearchScreenChild(val component: MainSearchComponent) : TopLevelChild()
            data class SearchedElementChild(val component: SearchedElementComponent) :
                TopLevelChild()
        }

        data object ProfileScreenChild : TopLevelChild() {
            data class MainProfileScreenChild(val component: MainProfileComponent) : TopLevelChild()
            data class ProfileHistoryScreenChild(val component: ProfileHistoryComponent) :
                TopLevelChild()

            data class SettingsScreenChild(val component: SettingsComponent) : TopLevelChild()
        }
    }

    @Serializable
    sealed interface TopLevelConfiguration {
        val topLevelType: TopLevelType

        @Serializable
        sealed interface MainScreenConfiguration : TopLevelConfiguration {
            override val topLevelType: TopLevelType
                get() = TopLevelType.MAIN_SCREEN

            @Serializable
            data object MainNewsConfiguration : MainScreenConfiguration

            @Serializable
            data class NewsPageConfiguration(val topic: HotTopics) : MainScreenConfiguration
        }

        @Serializable
        sealed interface SearchScreenConfiguration : TopLevelConfiguration {
            override val topLevelType: TopLevelType
                get() = TopLevelType.SEARCH_SCREEN

            @Serializable
            data object MainSearchConfiguration : SearchScreenConfiguration

            @Serializable
            data class SearchedElementConfiguration(
                val cardModel: SearchCardModel,
                val contentType: SearchType
            ) :
                SearchScreenConfiguration
        }

        @Serializable
        sealed interface ProfileScreenConfiguration : TopLevelConfiguration {
            override val topLevelType: TopLevelType
                get() = TopLevelType.PROFILE_SCREEN

            @Serializable
            data object MainProfileConfiguration : ProfileScreenConfiguration

            @Serializable
            data object ProfileHistoryConfiguration : ProfileScreenConfiguration

            @Serializable
            data object SettingsProfileConfiguration : ProfileScreenConfiguration
        }
    }

    enum class TopLevelType {
        MAIN_SCREEN,
        SEARCH_SCREEN,
        PROFILE_SCREEN
    }

    override val currentLink = MutableStateFlow(
        (childStack.active.instance as? IWebUri)?.currentLink?.value ?: ""
    )
}

fun setupNapier() {
    Napier.base(DebugAntilog())
}