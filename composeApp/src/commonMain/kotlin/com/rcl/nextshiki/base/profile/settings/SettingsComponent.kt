package com.rcl.nextshiki.base.profile.settings

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.rcl.mr.MR.strings.settings_en_lang
import com.rcl.mr.MR.strings.settings_reset_lang
import com.rcl.mr.MR.strings.settings_ru_lang
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.locale.CustomLocale.currentMRLocale
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.collections.immutable.persistentListOf

class SettingsComponent(
    val navigator: StackNavigation<RootComponent.TopLevelConfiguration>
) {
    val supportedLanguageButtons = persistentListOf(
        LanguageButton(code = "ru", langName = settings_ru_lang),
        LanguageButton(code = "en", langName = settings_en_lang),
        LanguageButton(code = null, langName = settings_reset_lang),
    )
    fun returnToProfile() {
        navigator.pop()
    }

    fun setupLanguage(code: String?) {
        val mokoCode = if (code != null) {
            StringDesc.LocaleType.Custom(code)
        } else {
            StringDesc.LocaleType.System
        }
        StringDesc.localeType = mokoCode
        currentMRLocale.value = mokoCode
    }

    data class LanguageButton(
        val code: String?,
        val langName: StringResource,
    )
}