package com.rcl.nextshiki.base.profile.settings

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.locale.Locale
import com.rcl.nextshiki.locale.LocalizedString
import kotlinx.collections.immutable.persistentListOf

class SettingsComponent(
    val navigator: StackNavigation<RootComponent.TopLevelConfiguration>
) {
    val supportedLanguageButtons = persistentListOf(
        LanguageButton(code = "ru", name = {it.settings_ru_lang}),
        LanguageButton(code = "en", name = {it.settings_en_lang}),
        LanguageButton(code = null, name = {it.settings_reset_lang}),
    )
    fun returnToProfile() {
        navigator.pop()
    }

    fun setupLanguage(code: String?) {
        Locale.set(code)
    }

    data class LanguageButton(
        val code: String?,
        val name: (LocalizedString) -> String,
    )
}