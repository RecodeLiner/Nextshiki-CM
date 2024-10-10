package com.rcl.nextshiki.components.profilecomponent.historypage

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.rcl.nextshiki.components.RootComponent

class ProfileHistoryComponent(
    context: ComponentContext,
    private val navigator: StackNavigation<RootComponent.TopLevelConfiguration>
) : ComponentContext by context {
    fun navigateBack() {
        navigator.pop()
    }
}
