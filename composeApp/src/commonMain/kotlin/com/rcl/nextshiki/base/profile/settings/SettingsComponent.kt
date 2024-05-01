package com.rcl.nextshiki.base.profile.settings

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.locale.Locale

class SettingsComponent(
    val navigator: StackNavigation<RootComponent.TopLevelConfiguration>
) {
    fun returnToProfile() {
        navigator.pop()
    }

    fun setupLanguage(code: String?) {
        Locale.set(code)
    }
}