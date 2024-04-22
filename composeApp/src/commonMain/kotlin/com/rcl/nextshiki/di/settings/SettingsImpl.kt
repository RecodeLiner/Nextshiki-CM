package com.rcl.nextshiki.di.settings

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

class SettingsImpl(val settings: Settings) : SettingsRepo {
    override fun addValue(key: String, value: String) {
        settings[key] = value
    }

    override fun removeValue(key: String) {
        settings.remove(key)
    }

    override fun getValue(key: String): String? {
        return settings[key]
    }

    override fun updateValue(key: String, value: String) {
        settings[key] = value
    }
}