package com.rcl.nextshiki.di.settings

import com.russhwolf.settings.Settings
import org.koin.dsl.module

object SettingsModule {
    val settingsModule = module {
        single {
            Settings()
        }
        single<SettingsRepo> {
            SettingsImpl(get())
        }
    }
}