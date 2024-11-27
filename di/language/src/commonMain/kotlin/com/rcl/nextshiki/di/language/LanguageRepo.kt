package com.rcl.nextshiki.di.language

import com.rcl.nextshiki.di.settings.ISettingsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class LanguageRepo(private val settings: ISettingsRepo) {
    val localeVar = MutableStateFlow("")

    init {
        updateLocale()
    }

    fun getCodeFromSettings(): String? {
        return settings.getValue("langCode")
    }

    fun getCurrentCode(): String {
        return getCodeFromSettings() ?: getCurrentLanguage()
    }

    fun setCodeToSettings(code: String?) {
        if (code == null) {
            settings.removeValue("langCode")
        } else {
            settings.addValue("langCode", code)
        }
    }

    fun updateLocale() {
        localeVar.update {
            getCurrentCode()
        }
    }
}