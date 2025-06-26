package com.rcl.nextshiki.features.di.modules

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rcl.nextshiki.presentation.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeColor(private val prefs: DataStore<Preferences>) {

    private val themeKey = stringPreferencesKey("theme_mode")
    private val themeColor = stringPreferencesKey("theme_color_hex")

    val currentThemeMode: Flow<ThemeMode> = prefs.data
        .map { preferences ->
            val themeName = preferences[themeKey] ?: ThemeMode.SYSTEM.name
            ThemeMode.valueOf(themeName)
        }

    val currentSelectedThemeColor: Flow<String?> = prefs.data
        .map { preferences ->
            preferences[themeColor]
        }

    suspend fun setThemeMode(themeMode: ThemeMode?) {
        prefs.edit { settings ->
            if (themeMode == null) {
                settings.remove(themeKey)
                return@edit
            }
            else {
                settings[themeKey] = themeMode.name
            }
        }
    }

    suspend fun setSelectedThemeColor(color: String?) {
        prefs.edit { colors ->
            if (color == null) {
                colors.remove(themeKey)
                return@edit
            }
            else {
                colors[themeColor] = color
            }
        }
    }
}