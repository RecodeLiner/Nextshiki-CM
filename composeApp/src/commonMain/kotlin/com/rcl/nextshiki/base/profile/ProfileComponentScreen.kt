package com.rcl.nextshiki.base.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rcl.nextshiki.base.profile.mainprofile.MainProfileComponentScreen
import com.rcl.nextshiki.base.profile.settings.SettingsComponentScreen

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun ProfileComponentScreen(component: ProfileComponent) {
    val stack by component.childStack.subscribeAsState()

    Children(
        stack = stack,
        animation = predictiveBackAnimation(
            backHandler = component::backHandler.get(),
            onBack = {
                component.onBack()
            },
        ),
    ) { searchLevelChild ->
        when (val instance = searchLevelChild.instance) {
            is ProfileComponent.ProfileLevelChild.MainProfileScreen -> MainProfileComponentScreen(instance.component)

            is ProfileComponent.ProfileLevelChild.SettingsScreen -> SettingsComponentScreen(instance.component)
        }
    }
}