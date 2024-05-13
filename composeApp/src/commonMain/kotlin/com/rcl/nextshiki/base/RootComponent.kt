package com.rcl.nextshiki.base

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.essenty.lifecycle.doOnStart
import com.rcl.nextshiki.base.main.mainpage.MainNewsComponent
import com.rcl.nextshiki.base.main.newspage.NewsPageComponent
import com.rcl.nextshiki.base.profile.historypage.ProfileHistoryComponent
import com.rcl.nextshiki.base.profile.mainprofile.MainProfileComponent
import com.rcl.nextshiki.base.profile.settings.SettingsComponent
import com.rcl.nextshiki.base.profile.settings.setupLanguage
import com.rcl.nextshiki.base.search.mainsearchscreen.MainSearchComponent
import com.rcl.nextshiki.base.search.mainsearchscreen.SearchType
import com.rcl.nextshiki.base.search.searchedelementscreen.SearchedElementComponent
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.di.settings.SettingsRepo
import com.rcl.nextshiki.elements.updateToken
import com.rcl.nextshiki.models.topics.HotTopics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RootComponent(context: ComponentContext) : ComponentContext by context, KoinComponent {
    private val ktorRepository: KtorRepository by inject()
    private val settings: SettingsRepo by inject()
    private val navigator = StackNavigation<TopLevelConfiguration>()

    init {
        lifecycle.doOnStart {
            val langCode = settings.getValue("langCode")
            when(langCode) {
                "en" -> {
                    setupLanguage("en", settings)
                }
                "ru" -> {
                    setupLanguage("ru", settings)
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                updateToken(ktorRepository, settings)
            }
        }
    }

    fun onBack() {
        navigator.pop()
    }

    fun navigateTo(config: TopLevelConfiguration) {
        navigator.navigate(
            transformer = { stack -> stack.filterNot { it == config } + config }
        )
    }

    val childStack = childStack(
        source = navigator,
        serializer = TopLevelConfiguration.serializer(),
        initialConfiguration = TopLevelConfiguration.MainScreenConfiguration.MainNews,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(
        config: TopLevelConfiguration,
        context: ComponentContext,
    ): TopLevelChild {
        return when (config) {
            is TopLevelConfiguration.MainScreenConfiguration.MainNews -> TopLevelChild.MainScreen.MainNews(
                MainNewsComponent(context = context, navigator = navigator)
            )
            is TopLevelConfiguration.MainScreenConfiguration.NewsPage -> TopLevelChild.MainScreen.NewsPage(
                NewsPageComponent(context = context, navigator = navigator, topic = config.topic)
            )

            is TopLevelConfiguration.SearchScreenConfiguration.MainSearchScreen -> TopLevelChild.SearchScreen.MainSearchScreen(
                MainSearchComponent(context = context, navigator = navigator)
            )
            is TopLevelConfiguration.SearchScreenConfiguration.SearchedElementScreen -> TopLevelChild.SearchScreen.SearchedElementScreen(
                SearchedElementComponent(context = context, contentType = config.contentType, id = config.id, navigator = navigator)
            )

            is TopLevelConfiguration.ProfileScreenConfiguration.MainProfileScreen -> TopLevelChild.ProfileScreen.MainProfileScreen(
                MainProfileComponent(context = context, navigator = navigator)
            )
            is TopLevelConfiguration.ProfileScreenConfiguration.ProfileHistoryScreen -> TopLevelChild.ProfileScreen.ProfileHistoryScreen(
                ProfileHistoryComponent(context = context, navigator = navigator)
            )
            is TopLevelConfiguration.ProfileScreenConfiguration.SettingsProfileScreen -> TopLevelChild.ProfileScreen.SettingsScreen(
                SettingsComponent(navigator = navigator)
            )

        }
    }

    sealed class TopLevelChild {
        data object MainScreen : TopLevelChild() {
            data class MainNews(val component: MainNewsComponent) : TopLevelChild()
            data class NewsPage(val component: NewsPageComponent) : TopLevelChild()
        }

        data object SearchScreen : TopLevelChild() {
            data class MainSearchScreen(val component: MainSearchComponent) : TopLevelChild()
            data class SearchedElementScreen(val component: SearchedElementComponent) : TopLevelChild()
        }
        data object ProfileScreen : TopLevelChild() {
            data class MainProfileScreen(val component: MainProfileComponent) : TopLevelChild()
            data class ProfileHistoryScreen(val component: ProfileHistoryComponent) : TopLevelChild()
            data class SettingsScreen(val component: SettingsComponent) : TopLevelChild()
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
            data object MainNews: MainScreenConfiguration

            @Serializable
            data class NewsPage(val topic: HotTopics) : MainScreenConfiguration
        }

        @Serializable
        sealed interface SearchScreenConfiguration : TopLevelConfiguration {
            override val topLevelType: TopLevelType
                get() = TopLevelType.SEARCH_SCREEN

            @Serializable
            data object MainSearchScreen : SearchScreenConfiguration

            @Serializable
            data class SearchedElementScreen(val id: String, val contentType: SearchType) : SearchScreenConfiguration
        }

        @Serializable
        sealed interface ProfileScreenConfiguration : TopLevelConfiguration {
            override val topLevelType: TopLevelType
                get() = TopLevelType.PROFILE_SCREEN

            @Serializable
            data object MainProfileScreen : ProfileScreenConfiguration
            @Serializable
            data object ProfileHistoryScreen : ProfileScreenConfiguration

            @Serializable
            data object SettingsProfileScreen : ProfileScreenConfiguration
        }
    }
    enum class TopLevelType {
        MAIN_SCREEN,
        SEARCH_SCREEN,
        PROFILE_SCREEN
    }
}