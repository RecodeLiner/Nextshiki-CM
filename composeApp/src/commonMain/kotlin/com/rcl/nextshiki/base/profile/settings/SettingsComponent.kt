package com.rcl.nextshiki.base.profile.settings

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.rcl.mr.SharedRes.strings.settings_en_lang
import com.rcl.mr.SharedRes.strings.settings_reset_lang
import com.rcl.mr.SharedRes.strings.settings_ru_lang
import com.rcl.nextshiki.base.RootComponent
import com.rcl.nextshiki.di.clipboard.IClipboard
import com.rcl.nextshiki.di.settings.ISettingsRepo
import com.rcl.nextshiki.locale.CustomLocale.currentLocal
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.collections.immutable.persistentListOf
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Stable
class SettingsComponent(
    val navigator: StackNavigation<RootComponent.TopLevelConfiguration>
) : KoinComponent {
    private val settings: ISettingsRepo by inject()
    private val clipboard: IClipboard by inject()
    val supportedLanguageButtons = persistentListOf(
        LanguageButton(code = "ru", langName = settings_ru_lang),
        LanguageButton(code = "en", langName = settings_en_lang),
        LanguageButton(code = null, langName = settings_reset_lang),
    )

    fun returnToProfile() {
        navigator.pop()
    }

    fun setupSettingsLanguage(code: String?) {
        setupLanguage(code, settings)
    }

    @Stable
    data class LanguageButton(
        val code: String?,
        val langName: StringResource,
    )

    fun copyToClipboard(text: String) {
        clipboard.copyToClipboard(text)
    }
}

fun setupLanguage(code: String?, settings: ISettingsRepo) {
    val mokoCode: StringDesc.LocaleType
    if (code != null) {
        mokoCode = StringDesc.LocaleType.Custom(code)
        settings.addValue(key = "langCode", value = code)
    } else {
        mokoCode = StringDesc.LocaleType.System
        settings.removeValue("langCode")
    }
    StringDesc.localeType = mokoCode
    currentLocal.value = Pair(mokoCode, code)
}
