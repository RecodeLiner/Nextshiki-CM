package com.rcl.nextshiki.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.rcl.nextshiki.locale.en.EnLocaleString
import com.rcl.nextshiki.locale.ru.RuLocaleString

object Locale {
    private val currentLocale = mutableStateOf("")

    private val localizedTextMap = mapOf(
        "en" to EnLocaleString,
        "ru" to RuLocaleString
    )

    fun set(locale: String?) {
        currentLocale.value = locale?: ""
    }

    @Suppress("unused", "for non-compose context")
    fun getLocalizedText(): LocalizedString {
        return localizedTextMap[currentLocale.value] ?: EnLocaleString
    }

    @Composable
    fun getComposeLocalizedText(): LocalizedString {
        val deviceLocale = remember { androidx.compose.ui.text.intl.Locale.current.language }
        val selectedLocale = currentLocale.value.ifEmpty { deviceLocale }
        return localizedTextMap[selectedLocale] ?: EnLocaleString
    }
}