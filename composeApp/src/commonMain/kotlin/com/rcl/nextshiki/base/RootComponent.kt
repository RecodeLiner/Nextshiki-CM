package com.rcl.nextshiki.base

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnStart
import com.rcl.nextshiki.base.main.MainComponent
import com.rcl.nextshiki.base.profile.ProfileComponent
import com.rcl.nextshiki.base.search.SearchComponent
import com.rcl.nextshiki.di.ktor.KtorRepository
import com.rcl.nextshiki.di.settings.SettingsRepo
import com.rcl.nextshiki.elements.updateToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RootComponent(context: ComponentContext) : ComponentContext by context, KoinComponent, WebResourceConstitute {
    private val ktorRepository: KtorRepository by inject()
    private val settings: SettingsRepo by inject()
    private val coroutine = CoroutineScope(Dispatchers.IO)
    private val navigator = StackNavigation<TopLevelConfiguration>()

    init {
        lifecycle.doOnStart {
            coroutine.launch {
                updateToken(ktorRepository, settings)
            }
        }
    }

    val childStack = childStack(
        source = navigator,
        serializer = TopLevelConfiguration.serializer(),
        initialConfiguration = TopLevelConfiguration.MainScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    fun navigateTo(config: TopLevelConfiguration) {
        navigator.bringToFront(config)
    }

    fun onBack() {
        navigator.pop()
    }

    private fun createChild(
        config: TopLevelConfiguration,
        context: ComponentContext,
    ): TopLevelChild {
        return when (config) {
            TopLevelConfiguration.MainScreen -> TopLevelChild.MainScreen(
                MainComponent(context = context)
            )
            TopLevelConfiguration.SearchScreen -> TopLevelChild.SearchScreen(
                SearchComponent(context = context)
            )
            TopLevelConfiguration.ProfileScreen -> TopLevelChild.ProfileScreen(
                ProfileComponent(context = context)
            )
        }
    }

    sealed class TopLevelChild {
        data class MainScreen(val component: MainComponent) : TopLevelChild()
        data class SearchScreen(val component: SearchComponent) : TopLevelChild()
        data class ProfileScreen(val component: ProfileComponent) : TopLevelChild()
    }

    @Serializable
    sealed interface TopLevelConfiguration {

        @Serializable
        data object MainScreen : TopLevelConfiguration

        @Serializable
        data object SearchScreen : TopLevelConfiguration

        @Serializable
        data object ProfileScreen : TopLevelConfiguration
    }

    override val webUri: MutableValue<String>?
        get() = (childStack.active.instance as? WebResourceConstitute)?.webUri
}