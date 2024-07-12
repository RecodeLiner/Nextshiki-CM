package com.rcl.nextshiki.di.settings

import com.russhwolf.settings.Settings
import org.koin.dsl.module

object SettingsModuleObject {
    val settingsModule = module {
        single {
            Settings()
        }
        single<ISettingsRepo> {
            SettingsImpl(get())
        }
    }
}
