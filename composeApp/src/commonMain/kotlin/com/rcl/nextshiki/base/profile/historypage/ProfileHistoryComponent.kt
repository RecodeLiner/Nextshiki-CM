package com.rcl.nextshiki.base.profile.historypage

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.rcl.nextshiki.base.RootComponent

@Stable
class ProfileHistoryComponent(
    context: ComponentContext,
    private val navigator: StackNavigation<RootComponent.TopLevelConfiguration>
) : ComponentContext by context {
    fun navigateBack() {
        navigator.pop()
    }
}
