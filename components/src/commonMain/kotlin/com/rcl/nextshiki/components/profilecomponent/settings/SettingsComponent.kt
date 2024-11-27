package com.rcl.nextshiki.components.profilecomponent.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.rcl.nextshiki.SharedRes.strings.settings_en_lang
import com.rcl.nextshiki.SharedRes.strings.settings_reset_lang
import com.rcl.nextshiki.SharedRes.strings.settings_ru_lang
import com.rcl.nextshiki.components.RootComponent
import com.rcl.nextshiki.di.clipboard.ClipboardImpl
import com.rcl.nextshiki.di.clipboard.IClipboard
import com.rcl.nextshiki.di.language.LanguageModule
import com.rcl.nextshiki.di.language.LanguageRepo
import dev.icerock.moko.resources.StringResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SettingsComponent(
    context: ComponentContext,
    val navigator: StackNavigation<RootComponent.TopLevelConfiguration>
) : ComponentContext by context {
    val vm = instanceKeeper.getOrCreate { SettingsViewModel() }

    fun returnToProfile() {
        navigator.pop()
    }

    class SettingsViewModel : InstanceKeeper.Instance {
        private val clipboard: IClipboard = ClipboardImpl()
        private val language: LanguageRepo = LanguageModule.langRepo
        private val currentCode = MutableStateFlow(language.getCurrentCode())

        val supportedLanguageButtons = listOf(
            LanguageButton(code = "ru", langName = settings_ru_lang),
            LanguageButton(code = "en", langName = settings_en_lang),
            LanguageButton(code = null, langName = settings_reset_lang),
        )

        fun setupSettingsLanguage(code: String?) {
            language.setCodeToSettings(code)
            currentCode.update {
                language.getCurrentCode()
            }
        }

        data class LanguageButton(
            val code: String?,
            val langName: StringResource,
        )

        fun copyToClipboard(text: String) {
            clipboard.copyToClipboard(text)
        }
    }
}