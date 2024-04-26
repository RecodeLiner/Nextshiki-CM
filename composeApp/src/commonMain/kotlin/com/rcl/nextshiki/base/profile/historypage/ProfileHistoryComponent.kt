package com.rcl.nextshiki.base.profile.historypage

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.rcl.nextshiki.base.profile.ProfileComponent

class ProfileHistoryComponent(
    context: ComponentContext,
    private val navigator: StackNavigation<ProfileComponent.ProfileConfiguration>
) : ComponentContext by context {
    fun navigateBack() {
        navigator.pop()
    }
}