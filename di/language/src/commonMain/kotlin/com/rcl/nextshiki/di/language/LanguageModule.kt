package com.rcl.nextshiki.di.language

import com.rcl.nextshiki.di.settings.SettingsModuleObject

object LanguageModule {
    val langRepo = LanguageRepo(SettingsModuleObject.settingsImpl)
}