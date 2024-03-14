package com.rcl.nextshiki.base.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.rcl.nextshiki.base.WebResourceConstitute
import com.rcl.nextshiki.base.profile.mainprofile.MainProfileComponent
import com.rcl.nextshiki.base.profile.settings.SettingsComponent
import kotlinx.serialization.Serializable

class ProfileComponent(context: ComponentContext) : ComponentContext by context, WebResourceConstitute {
    private val navigator = StackNavigation<ProfileConfiguration>()

    private fun navigateTo(config: ProfileConfiguration) {
        navigator.bringToFront(config)
    }

    val childStack = childStack(
        source = navigator,
        serializer = ProfileConfiguration.serializer(),
        initialConfiguration = ProfileConfiguration.MainProfileScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    fun onBack() {
        navigator.pop()
    }

    private fun createChild(
        config: ProfileConfiguration,
        context: ComponentContext
    ): ProfileLevelChild {
        return when (config) {
            ProfileConfiguration.MainProfileScreen -> ProfileLevelChild.MainProfileScreen(
                MainProfileComponent(
                    context = context,
                    navToSettings = { navigateTo(ProfileConfiguration.SettingsProfileScreen) },
                    navBack = { navigator.pop() }
                )
            )

            ProfileConfiguration.SettingsProfileScreen -> ProfileLevelChild.SettingsScreen(
                SettingsComponent(
                    navigateToProfile = {
                        navigateTo(ProfileConfiguration.MainProfileScreen)
                    }
                )
            )
        }
    }

    sealed class ProfileLevelChild {
        data class MainProfileScreen(val component: MainProfileComponent) : ProfileLevelChild()
        data class SettingsScreen(val component: SettingsComponent) : ProfileLevelChild()
    }

    @Serializable
    sealed interface ProfileConfiguration {
        @Serializable
        data object MainProfileScreen : ProfileConfiguration

        @Serializable
        data object SettingsProfileScreen : ProfileConfiguration
    }

    override val webUri: String?
        get() = null
}