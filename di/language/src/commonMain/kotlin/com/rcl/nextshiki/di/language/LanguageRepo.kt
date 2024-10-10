package com.rcl.nextshiki.di.language

import com.arkivanov.decompose.value.MutableValue
import com.rcl.nextshiki.di.settings.ISettingsRepo

class LanguageRepo(private val settings: ISettingsRepo) {
    val localeVar = MutableValue("")
    init {
        updateLocale()
    }
    fun getCodeFromSettings(): String? {
        return settings.getValue("langCode")
    }
    fun getCurrentCode() : String {
        return getCodeFromSettings()?: getCurrentLanguage()
    }
    fun setCodeToSettings(code: String?) {
        if (code == null) {
            settings.removeValue("langCode")
        }
        else {
            settings.addValue("langCode", code)
        }
    }
    fun updateLocale() {
        localeVar.value = getCurrentCode()
    }
}