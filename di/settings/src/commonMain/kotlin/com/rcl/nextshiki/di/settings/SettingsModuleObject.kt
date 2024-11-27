package com.rcl.nextshiki.di.settings

import com.russhwolf.settings.Settings

object SettingsModuleObject {
    val settings = Settings()
    val settingsImpl = SettingsImpl(settings)
}
